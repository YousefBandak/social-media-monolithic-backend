package object_orienters.techspot.service;

import java.util.List;

import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Profile;

public interface ProfileService {
    public Profile getUserByUsername(String username) throws UserNotFoundException;

    public Profile createNewUser(Profile newUser);

    public Profile updateUserProfile(Profile newUser, String username) throws UserNotFoundException;

    public List<Profile> getUserFollowersByUsername(String username) throws UserNotFoundException;

    public Profile getFollowerByUsername(String username, String followerUsername) throws UserNotFoundException;

    public List<Profile> getUserFollowingByUsername(String username) throws UserNotFoundException;

    public Profile getFollowingByUsername(String username, String followingUsername) throws UserNotFoundException;

    public Profile addNewFollower(String username, Profile newFollower) throws UserNotFoundException;

    public void deleteFollower(String username) throws UserNotFoundException;

    public Profile addNewFollowing(String username, Profile newFollowing) throws UserNotFoundException;

    public void deleteFollowing(String username) throws UserNotFoundException;

}
