package object_orienters.techspot.profile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    public Profile getUserByUsername(String username) throws UserNotFoundException;

    public Profile createNewProfile(String username, String email, String name, MultipartFile file) throws IOException;
    public Profile updateUserProfile(Profile newUser, String username) throws UserNotFoundException;

    public List<Profile> getUserFollowersByUsername(String username) throws UserNotFoundException;

    public Profile getFollowerByUsername(String username, String followerUsername) throws UserNotFoundException;

    public List<Profile> getUserFollowingByUsername(String username) throws UserNotFoundException;

    public Optional<Profile> getFollowingByUsername(String username, String followingUsername) throws UserNotFoundException;

    public Profile addNewFollower(String username, String newFollower) throws UserNotFoundException;

    public void deleteFollower(String username, String deletedUser) throws UserNotFoundException;

    public void deleteFollowing(String username, String deletedUser) throws UserNotFoundException;

    public void deleteProfile(String username) throws UserNotFoundException;

    public Profile addProfilePic(String username, MultipartFile file, String text)
            throws UserNotFoundException, IOException;

}
