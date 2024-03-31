package object_orienters.techspot.security;

import jakarta.validation.Valid;
import object_orienters.techspot.comment.CommentController;
import object_orienters.techspot.profile.ImpleProfileService;
import object_orienters.techspot.profile.ProfileNotFoundException;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.security.jwt.JwtUtils;
import object_orienters.techspot.security.model.User;
import object_orienters.techspot.security.payload.JwtResponse;
import object_orienters.techspot.security.payload.LoginRequest;
import object_orienters.techspot.security.payload.MessageResponse;
import object_orienters.techspot.security.payload.SignupRequest;
import object_orienters.techspot.security.repository.UserRepository;
import object_orienters.techspot.security.service.ImpleUserDetails;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
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
    ImpleProfileService profileService;

    @Autowired
    ProfileRepository profileRepository;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(CommentController.class);

    @GetMapping("/home")
    public String home() {
        return "Hello, Home!";
    }

    @GetMapping("/usernameExists/{username}")
    public ResponseEntity<?> usernameExists(@PathVariable String username) {
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("Username is already taken!"));
        } else {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("Username is available!"));
        }
    }

    @PostMapping("/signin") //TODO: Change to /login
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        ImpleUserDetails userDetails = (ImpleUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));

        // Set the lastLogin field to the current timestamp
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));

        // Save the updated User object back to the database
        userRepository.save(user);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        //roleRepository.save(new Role(ERole.ROLE_ADMIN));
        logger.info("Registering user");
        if (userCredentialsServices.usernameExists(signUpRequest)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            logger.info("2nd if");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));


//        userCredentialsServices.setRole(signUpRequest);
        userRepository.save(user);

        profileService.createNewProfile(user.getUsername(), user.getEmail(), signUpRequest.getName());
        logger.info("User Registered Successfully " + user.toString());
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));


    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody SignupRequest signUpRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof ImpleUserDetails userDetails) {
            String clientUsername = userDetails.getUsername();

            if (!signUpRequest.getUsername().equals(clientUsername) && userCredentialsServices.usernameExists(signUpRequest)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username is already taken!"));
            }

            if (!signUpRequest.getEmail().equals(userRepository.findByUsername(clientUsername).orElseThrow(() -> new UserNotFoundException(clientUsername)).getEmail())
                    && userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }

            User user = userRepository.findByUsername(clientUsername)
                    .orElseThrow(() -> new UserNotFoundException(clientUsername));
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));
            user.setUsername(signUpRequest.getUsername());
            userRepository.save(user); //NOte this create a new user object
            return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Unable to identify client user!"));


    }


    //TODO: Test this endpoint
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof ImpleUserDetails userDetails) {
            String username = userDetails.getUsername();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException(username));
            profileRepository.delete(profileRepository.findByUsername(username).orElseThrow(() -> new ProfileNotFoundException(username)));
            userRepository.delete(user);
            return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Unable to identify client user!"));
    }

}
