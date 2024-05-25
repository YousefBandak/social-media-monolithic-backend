package object_orienters.techspot.security;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import object_orienters.techspot.comment.CommentRepository;
import object_orienters.techspot.exceptions.ProfileNotFoundException;
import object_orienters.techspot.exceptions.TokenRefreshException;
import object_orienters.techspot.exceptions.UserNotFoundException;
import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.ProfileService;
import object_orienters.techspot.security.blacklist.ImpleTokenBlackListService;
import object_orienters.techspot.security.jwt.JwtUtils;
import object_orienters.techspot.security.model.RefreshToken;
import object_orienters.techspot.security.model.User;
import object_orienters.techspot.security.payload.request.LoginRequest;
import object_orienters.techspot.security.payload.request.SignupRequest;
import object_orienters.techspot.security.payload.request.TokenRefreshRequest;
import object_orienters.techspot.security.payload.response.JwtResponse;
import object_orienters.techspot.security.payload.response.MessageResponse;
import object_orienters.techspot.security.payload.response.TokenRefreshResponse;
import object_orienters.techspot.security.repository.RefreshTokenRepository;
import object_orienters.techspot.security.repository.UserRepository;
import object_orienters.techspot.security.service.ImpleUserDetails;
import object_orienters.techspot.security.service.RefreshTokenService;
import object_orienters.techspot.utilities.FileStorageService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {
        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        UserRepository userRepository;

        @Autowired
        PasswordEncoder encoder;

        @Autowired
        JwtUtils jwtUtils;

        @Autowired
        UserCredentialsServices userCredentialsServices;

        @Autowired
        ProfileService profileService;

        @Autowired
        ProfileRepository profileRepository;

        @Autowired
        RefreshTokenService refreshTokenService;

        @Autowired
        ImpleTokenBlackListService blackListService;

        @Autowired
        PostRepository postRepository;

        @Autowired
        CommentRepository commentRepository;

        @Autowired
        RefreshTokenRepository refreshTokenRepository;

        @Autowired
        FileStorageService fileStorageService;
        private final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        @GetMapping("/home")
        public String home() {
                return "Hello, Home!";
        }

        @GetMapping("/usernameExists/{username}")
        public ResponseEntity<?> usernameExists(@PathVariable String username) {
                logger.info(">>>>Checking if Username Already Exists... @ " + getTimestamp() + "<<<<");
                if (userRepository.existsByUsername(username)) {
                        logger.info(">>>>Username Already Exists. @ " + getTimestamp() + "<<<<");
                        return ResponseEntity
                                        .ok()
                                        .body(new MessageResponse("Username is already taken!"));
                } else {
                        logger.info(">>>>Username Available. @ " + getTimestamp() + "<<<<");
                        return ResponseEntity
                                        .ok()
                                        .body(new MessageResponse("Username is available!"));
                }
        }

        @PostMapping("/login")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
                logger.info(">>>>Authenticating User... @ " + getTimestamp() + "<<<<");
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                                loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                ImpleUserDetails userDetails = (ImpleUserDetails) authentication.getPrincipal();

                String jwt = jwtUtils.generateJwtToken(authentication);

                // List<String> roles = userDetails.getAuthorities().stream()
                // .map(item -> item.getAuthority())
                // .collect(Collectors.toList());

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

                User user = userRepository.findByUsername(userDetails.getUsername())
                                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));

                // Set the lastLogin field to the current timestamp
                user.setLastLogin(new Timestamp(System.currentTimeMillis()));

                // Save the updated User object back to the database
                userRepository.save(user);

                logger.info(">>>>User " + user + " Authenticated Successfully. @ " + getTimestamp()
                                + "<<<<");
                return ResponseEntity.ok(new JwtResponse(
                                jwt,
                                refreshToken.getToken(),
                                userDetails.getUsername(),
                                userDetails.getEmail()));
        }

        @PostMapping("/refreshtoken")
        public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
                String requestRefreshToken = request.getRefreshToken();

                return refreshTokenService.findByToken(requestRefreshToken)
                                .map(refreshTokenService::verifyExpiration)
                                .map(RefreshToken::getUser)
                                .map(user -> {
                                        String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                                        return ResponseEntity.ok(new TokenRefreshResponse(token, refreshTokenService
                                                        .createRefreshToken(user.getUsername()).getToken()));
                                })
                                .orElseThrow(() -> {
                                        logger.info(">>>>Error Occurred: Refresh token is not in database! @ "
                                                        + getTimestamp() + "<<<<");
                                        // log out
                                        SecurityContextHolder.clearContext();
                                        // delete refresh token form db for user
                                        return new TokenRefreshException(requestRefreshToken,
                                                        "Refresh token is not in database!, Please login again");
                                });
        }

        @PostMapping("/signup")
        public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws IOException {
                logger.info(">>>>Regsitering User... @ " + getTimestamp() + "<<<<");
                if (userCredentialsServices.usernameExists(signUpRequest)) {
                        logger.info(
                                        ">>>>Username " + signUpRequest.getUsername() + " Already Exists. @ "
                                                        + getTimestamp() + "<<<<");
                        return ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponse("Error: Username is already taken!"));
                }

                if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                        logger.info(">>>>Email " + signUpRequest.getEmail() + " Already Exists. @ " + getTimestamp()
                                        + "<<<<");
                        return ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponse("Error: Email is already in use!"));
                }

                // Create new user's account
                User user = new User(signUpRequest.getUsername(),
                                signUpRequest.getEmail(),
                                encoder.encode(signUpRequest.getPassword()));

                // userCredentialsServices.setRole(signUpRequest);
                userRepository.save(user);
                profileService.createNewProfile(user.getUsername(), user.getEmail(), signUpRequest.getName(), null);
                logger.info(">>>>User " + user + " Registered Successfully. @ " + getTimestamp() + "<<<<");
                return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }

        @PostMapping("/logout")
        public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String tokenHeader) {
                if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                        String token = tokenHeader.substring(7);
                        blackListService.blacklistToken(token);
                        SecurityContextHolder.clearContext();
                        logger.info(">>>>User Logged Out Successfully. @ " + LocalDateTime.now() + "<<<<");
                        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
                }
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid token."));
        }

        @PutMapping("/update")
        public ResponseEntity<?> updateUser(@Valid @RequestBody SignupRequest signUpRequest) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                logger.info(">>>>Updating User... @ " + getTimestamp() + "<<<<");
                if (principal instanceof ImpleUserDetails userDetails) {
                        String clientUsername = userDetails.getUsername();

                        if (!signUpRequest.getUsername().equals(clientUsername)
                                        && userCredentialsServices.usernameExists(signUpRequest)) {
                                logger.info(
                                                ">>>>Username " + signUpRequest.getUsername() + " Already Exists. @ "
                                                                + getTimestamp()
                                                                + "<<<<");
                                return ResponseEntity
                                                .badRequest()
                                                .body(new MessageResponse("Error: Username is already taken!"));
                        }

                        if (!signUpRequest.getEmail()
                                        .equals(userRepository.findByUsername(clientUsername)
                                                        .orElseThrow(() -> new UserNotFoundException(clientUsername))
                                                        .getEmail())
                                        && userRepository.existsByEmail(signUpRequest.getEmail())) {
                                logger.info(">>>>Email " + signUpRequest.getEmail() + " Already Exists. @ "
                                                + getTimestamp() + "<<<<");
                                return ResponseEntity
                                                .badRequest()
                                                .body(new MessageResponse("Error: Email is already in use!"));
                        }

                        User user = userRepository.findByUsername(clientUsername)
                                        .orElseThrow(() -> new UserNotFoundException(clientUsername));
                        user.setEmail(signUpRequest.getEmail());
                        user.setPassword(encoder.encode(signUpRequest.getPassword()));
                        user.setUsername(signUpRequest.getUsername());
                        userRepository.save(user);
                        logger.info(">>>>User " + user + " Updated Successfully. @ " + getTimestamp()
                                        + "<<<<");
                        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
                }
                logger.info(">>>>Error Occurred: Unable to Identify Client User. @ " + getTimestamp() + "<<<<");
                return ResponseEntity
                                .badRequest()
                                .body(new MessageResponse("Error: Unable to identify client user!"));

        }


        @DeleteMapping("/delete")
        @Transactional
        public ResponseEntity<?> deleteUser() {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                logger.info(">>>>Deleting User... @ " + getTimestamp() + "<<<<");
                if (principal instanceof ImpleUserDetails userDetails) {
                        String username = userDetails.getUsername();
                        User user = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new UserNotFoundException(username));
                        Profile profile = profileRepository.findByUsername(username)
                                        .orElseThrow(() -> new ProfileNotFoundException(username));
                        // commentRepository.deleteAllByCommentAuthorUsername(username);
                        
                        profile.getPublishedPosts().stream().forEach(post -> {
                                post.getMediaData().stream().forEach(media -> {
                                        fileStorageService.deleteFile(media.getFileName());
                                });
                                postRepository.delete(post);
                        });
                        refreshTokenRepository.deleteByUser(user);
                        userRepository.delete(user);
                        profileRepository.delete(profile);
                        logger.info(">>>>User Deleted Successfully. @ " + getTimestamp() + "<<<<");
                        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
                }
                logger.info(">>>>Error Occurred: Unable to Identify Client User. @ " + getTimestamp() + "<<<<");
                return ResponseEntity
                                .badRequest()
                                .body(new MessageResponse("Error: Unable to identify client user!"));
        }

        private static String getTimestamp() {
                return LocalDateTime.now().format(formatter) + " ";
        }

}
