package object_orienters.techspot.security;

import object_orienters.techspot.security.payload.request.SignupRequest;
import object_orienters.techspot.security.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsServices {

    private final UserRepository userRepository;

    public UserCredentialsServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean usernameExists(SignupRequest signUpRequest) {
        return userRepository.existsByUsername(signUpRequest.getUsername());
    }

    public boolean emailExists(SignupRequest signUpRequest) {
        return userRepository.existsByEmail(signUpRequest.getEmail());
    }
}
