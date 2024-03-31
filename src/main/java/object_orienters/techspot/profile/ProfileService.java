package object_orienters.techspot.profile;

import java.util.List;

public interface ProfileService {
    public Profile getUserByUsername(String username) throws UserNotFoundException;

    public Profile createNewProfile(Profile newUser);
    public Profile createNewProfile(String username, String email, String name);
    public Profile updateUserProfile(Profile newUser, String username) throws UserNotFoundException;

    public List<Profile> getUserFollowersByUsername(String username) throws UserNotFoundException;

    public Profile getFollowerByUsername(String username, String followerUsername) throws UserNotFoundException;

    public List<Profile> getUserFollowingByUsername(String username) throws UserNotFoundException;

    public Profile getFollowingByUsername(String username, String followingUsername) throws UserNotFoundException;

    public Profile addNewFollower(String username, String newFollower) throws UserNotFoundException;

    public void deleteFollower(String username, Profile deletedUser) throws UserNotFoundException;

}
