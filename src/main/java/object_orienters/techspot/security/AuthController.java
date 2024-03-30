package object_orienters.techspot.security;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import object_orienters.techspot.comment.CommentController;

import object_orienters.techspot.profile.ImpleProfileService;
import object_orienters.techspot.security.model.User;
import object_orienters.techspot.security.payload.JwtResponse;
import object_orienters.techspot.security.payload.LoginRequest;
import object_orienters.techspot.security.payload.MessageResponse;
import object_orienters.techspot.security.payload.SignupRequest;
import object_orienters.techspot.security.repository.RoleRepository;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserCredentialsServices userCredentialsServices;

    @Autowired
    ImpleProfileService profileService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(CommentController.class);

    @PostMapping("/home")
    public String home() {
        return "Hello, Home!";
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        ImpleUserDetails userDetails = (ImpleUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

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





        user.setRoles(Set.of(userCredentialsServices.setRole(signUpRequest)));
        userRepository.save(user);

        profileService.createNewProfile(user.getUsername(), user.getEmail(), signUpRequest.getName());
        logger.info("User Registered Successfully " + user.toString());
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
