package object_orienters.techspot.security;

import jakarta.validation.Valid;
import object_orienters.techspot.exceptions.*;
import object_orienters.techspot.security.jwt.JwtUtils;
import object_orienters.techspot.security.model.*;
import object_orienters.techspot.security.payload.request.LoginRequest;
import object_orienters.techspot.security.payload.request.SignupRequest;
import object_orienters.techspot.security.payload.request.TokenRefreshRequest;
import object_orienters.techspot.security.payload.response.JwtResponse;
import object_orienters.techspot.security.payload.response.MessageResponse;
import object_orienters.techspot.security.payload.response.TokenRefreshResponse;
import object_orienters.techspot.security.service.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AuthController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private OAuth2Services oAuth2Services;

    @Autowired
    private GoogleOAuth googleOAuth;

    @Autowired
    private GithubOAuth githubOAuth;

    @Autowired
    private GeneralAuthServices generalAuthServices;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @PostMapping("/auth/oauth/signup")
    public ResponseEntity<?> registerUserOAuth(@RequestBody OAuthDto oAuthDto) throws IOException {
        logger.info(">>>>Regsitering OAuth User... @ " + getTimestamp() + "<<<<");
        try {
            JwtResponse jwtResponse = oAuth2Services.registerOuthUser(oAuthDto);
            return ResponseEntity.ok(jwtResponse);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            logger.info(">>>>Error Occurred: " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ExceptionsResponse.getErrorResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/oauth2/callback/google")
    public Object handleOAuth2Callback(@RequestParam("code") String authorizationCode) {
        String accessToken = googleOAuth.exchangeCodeForAccessTokenGoogle(authorizationCode);

        UserOAuthTemp userOAuthTemp = googleOAuth.getUserInfoGoogle(accessToken);

        if (oAuth2Services.isLogin(userOAuthTemp, Provider.GOOGLE)) {
            System.out.println("User is already registered");
            JwtResponse jwtResponse = oAuth2Services.loginOAuthUser(userOAuthTemp, Provider.GOOGLE);
            String redirectUrl = "http://localhost:3000/oauth2/login?token=" + jwtResponse.getToken() + "&refreshToken=" + jwtResponse.getRefreshToken() + "&username=" + jwtResponse.getUsername();
            return new RedirectView(redirectUrl);
        } else {
            System.out.println("User is not registered");
            String redirectUrl = oAuth2Services.saveUserOAuthTempAndRedirectURL(userOAuthTemp);
            return new RedirectView(redirectUrl);
        }


    }

    @GetMapping("/login/oauth2/code/github")
    public Object handleGitHubOAuth2Callback(@RequestParam String code) {
        String accessToken = githubOAuth.exchangeCodeForAccessTokenGithub(code);

        UserOAuthTemp userOAuthTemp = githubOAuth.getUserInfoGithub(accessToken);
        if (oAuth2Services.isLogin(userOAuthTemp, Provider.GITHUB)) {
            JwtResponse jwtResponse = oAuth2Services.loginOAuthUser(userOAuthTemp, Provider.GITHUB);
            String redirectUrl = "http://localhost:3000/oauth2/login?token=" + jwtResponse.getToken() + "&refreshToken=" + jwtResponse.getRefreshToken() + "&username=" + jwtResponse.getUsername();
            return new RedirectView(redirectUrl);

        } else {

            String redirectUrl = oAuth2Services.saveUserOAuthTempAndRedirectURL(userOAuthTemp);
            return new RedirectView(redirectUrl);
        }
    }


    @GetMapping("/auth/usernameExists/{username}")
    public ResponseEntity<?> usernameExists(@PathVariable String username) {
        logger.info(">>>>Checking if Username Already Exists... @ " + getTimestamp() + "<<<<");
        if (generalAuthServices.usernameExists(username)) {
            logger.info(">>>>Username Already Exists. @ " + getTimestamp() + "<<<<");
            return ResponseEntity
                    .badRequest()
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
        logger.info(">>>>Authenticating User... @ " + getTimestamp() + "<<<<");
        try {
            JwtResponse jwtResponse = generalAuthServices.authenticateUser(loginRequest);
            logger.info(">>>>User Authenticated Successfully. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(jwtResponse);
        } catch (UserNotFoundException e) {
            logger.info(">>>>Error Occurred: " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ExceptionsResponse.getErrorResponseEntity(e, HttpStatus.NOT_FOUND);

        }
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
        try {

            User user = generalAuthServices.registerUser(signUpRequest);

            logger.info(">>>>User " + user + " Registered Successfully. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            logger.info(">>>>Error Occurred: " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ExceptionsResponse.getErrorResponseEntity(e, HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String tokenHeader) {
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);

            generalAuthServices.logoutUser(token);

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
            try {

                User user = generalAuthServices.updateUser(clientUsername, signUpRequest);

                logger.info(">>>>User " + user + " Updated Successfully. @ " + getTimestamp()
                        + "<<<<");
                return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
            } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
                logger.info(">>>>Error Occurred: " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
                return ExceptionsResponse.getErrorResponseEntity(e, HttpStatus.BAD_REQUEST);
            }
        }
        logger.info(">>>>Error Occurred: Unable to Identify Client User. @ " + getTimestamp() + "<<<<");
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Unable to identify client user!"));
    }


    @DeleteMapping("/auth/delete")
    public ResponseEntity<?> deleteUser() {
        logger.info(">>>>Deleting User... @ " + getTimestamp() + "<<<<");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof ImpleUserDetails userDetails) {
            String username = userDetails.getUsername();

            generalAuthServices.deleteUser(username);

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
