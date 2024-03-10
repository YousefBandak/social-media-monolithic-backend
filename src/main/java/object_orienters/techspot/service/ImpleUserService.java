package object_orienters.techspot.service;

import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.User;
import object_orienters.techspot.repository.UserRepository;

public class ImpleUserService implements UserService{
    UserRepository userRepository;
    @Override
    public User getUserByUsername(String userName) throws UserNotFoundException {
        return userRepository.findById(userName).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
