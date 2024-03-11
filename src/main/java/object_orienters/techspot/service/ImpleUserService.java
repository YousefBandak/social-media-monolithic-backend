package object_orienters.techspot.service;

import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Profile;
import object_orienters.techspot.repository.ProfileRepository;

public class ImpleUserService implements UserService{
    ProfileRepository userRepository;
    @Override
    public Profile getUserByUsername(String userName) throws UserNotFoundException {
        return userRepository.findById(userName).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
