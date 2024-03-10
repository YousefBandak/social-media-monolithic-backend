package object_orienters.techspot.service;

import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.User;

public interface UserService {
    public User getUserByUsername(String userName) throws UserNotFoundException;
}
