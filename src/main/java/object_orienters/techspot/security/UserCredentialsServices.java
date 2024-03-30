package object_orienters.techspot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsServices {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserCredentialsServices(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public boolean usernameExists(SignupRequest signUpRequest) {
        return userRepository.existsByUsername(signUpRequest.getUsername());
    }

    public boolean emailExists(SignupRequest signUpRequest) {
        return userRepository.existsByEmail(signUpRequest.getEmail());
    }

    public Role setRole(SignupRequest signUpRequest) {
        String strRole = signUpRequest.getRole() != null ? signUpRequest.getRole().toString() : "user";
        Role role;
        switch (strRole) {
            case "admin":
                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                role = adminRole;
                break;
            default:
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                role = (userRole);

        }

        return role;
    }
}
