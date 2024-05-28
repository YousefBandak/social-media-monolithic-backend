package object_orienters.techspot.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
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


    private static final String CLIENT_ID = "53578245310-c2pi6chirmmqep6cn9q68jp21kao0vr6.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";


    private static final String CLIENT_ID_GIT = "Ov23liq6SrGiWeOzrddu";
    private static final String CLIENT_SECRET_GIT = "c6a8f8139914237aa7f7119355565a5a75c7fba8";


    @GetMapping("/auth/home")
    public String home() {
        System.out.println("I entered the home method");
        return "Hello, Home!";
    }


    @GetMapping("/oauth2/callback/google")
    public String handleOAuth2Callback(@RequestParam("code") String authorizationCode) {
        System.out.println("I entered the callback method");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", authorizationCode);
        map.add("redirect_uri", "http://localhost:8080/oauth2/callback/google");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, String.class);

        String accessToken = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode root = objectMapper.readTree(accessToken);
            accessToken = root.path("access_token").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("Access Token: " + accessToken);

        // Store the access token in a secure manner (not shown)//FIXME

        // Make an authenticated request
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange("https://www.googleapis.com/oauth2/v2/userinfo", HttpMethod.GET, entity, String.class);

        String responseBody = responseEntity.getBody();

        System.out.println(responseBody);
        return responseBody;
    }

    @GetMapping("/login/oauth2/code/github")
    public String handleGitHubOAuth2Callback(@RequestParam String code) {
        System.out.println("Entered GitHub callback method");
        System.out.println("Code: " + code);
        RestTemplate restTemplate = new RestTemplate();
        String authorizationCode = code;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", authorizationCode);
        map.add("redirect_uri", "http://localhost:8080/login/oauth2/code/github");
        map.add("client_id", CLIENT_ID_GIT);
        map.add("client_secret", CLIENT_SECRET_GIT);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        System.out.println("before exchange");

        ResponseEntity<String> response = restTemplate.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                request,
                String.class
        );

        System.out.println("after exchange ");

        String responseBody = response.getBody();
        System.out.println("Response Body: " + responseBody);

        String accessToken = null;
        try {
            for (String param : responseBody.split("&")) {
                String[] keyValue = param.split("=");
                if ("access_token".equals(keyValue[0])) {
                    accessToken = keyValue[1];
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing access token: " + e.getMessage();
        }

        System.out.println("GitHub Access Token: " + accessToken);

        // Store the access token in a secure manner (not shown)

        // Make an authenticated request to get user info
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                entity,
                String.class
        );

        String userResponse = responseEntity.getBody();

        System.out.println(userResponse);
        return userResponse;
    }



    @GetMapping("/auth/usernameExists/{username}")
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

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("I entered the login method");
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

    @PostMapping("/auth/refreshtoken")
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

    @PostMapping("/auth/signup")
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
        profileService.createNewProfile(user.getUsername(), user.getEmail(), signUpRequest.getName());
        logger.info(">>>>User " + user + " Registered Successfully. @ " + getTimestamp() + "<<<<");
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/auth/logout")
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

    @PutMapping("/auth/update")
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


    @DeleteMapping("/auth/delete")
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
