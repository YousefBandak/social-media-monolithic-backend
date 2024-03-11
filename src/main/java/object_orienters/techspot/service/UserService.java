package object_orienters.techspot.service;

import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Profile;

public interface UserService {
    public Profile getUserByUsername(String userName) throws UserNotFoundException;
}
