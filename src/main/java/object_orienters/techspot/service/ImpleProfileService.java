package object_orienters.techspot.service;

import java.util.List;
import java.util.Optional;

import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Profile;
import object_orienters.techspot.repository.ProfileRepo;
import object_orienters.techspot.service.interfaces.ProfileService;
import org.springframework.stereotype.Service;

@Service
public class ImpleProfileService implements ProfileService {
    private ProfileRepo repo;

    public ImpleProfileService(ProfileRepo repo) {
        this.repo = repo;
    }

    @Override
    public Profile getUserByUsername(String username) throws UserNotFoundException {
        return repo.findById(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public Profile createNewUser(Profile newUser) throws UserNotFoundException {
        return repo.save(newUser);
    }

    @Override
    public Profile updateUserProfile(Profile newUser, String username) throws UserNotFoundException {
        Profile updatedUser = repo.findById(username).map(user -> {
            user.setUsername(newUser.getUsername());
            user.setProfilePic(newUser.getProfilePic());
            user.setDob(newUser.getDob());
            user.setEmail(newUser.getEmail());
            user.setFollowers(newUser.getFollowers());
            user.setFollowing(newUser.getFollowing());
            user.setName(newUser.getName());
            user.setProffesion(newUser.getProffesion());
            user.setGender(newUser.getGender());
            user.setPublishedPosts(newUser.getPublishedPosts());
            user.setSharedPosts(newUser.getSharedPosts());
            return repo.save(user);
        }).orElseThrow(() -> new UserNotFoundException(username));
        return updatedUser;
    }

    @Override
    public List<Profile> getUserFollowersByUsername(String username) throws UserNotFoundException {
        return repo.findFollowersByUserId(username);
    }

    @Override
    public Profile getFollowerByUsername(String username, String followerUserName) throws UserNotFoundException {
        return repo.findFollowerByUsername(username, followerUserName);
    }

    @Override
    public List<Profile> getUserFollowingByUsername(String username) throws UserNotFoundException {
        return repo.findFollowingByUserId(username);
    }

    @Override
    public Profile getFollowingByUsername(String username, String followingUsername) throws UserNotFoundException {
        return repo.findFollowingByUsername(username, followingUsername);
    }

    @Override
    public Profile addNewFollower(String username, Profile newFollower) throws UserNotFoundException {
        Optional<Profile> user = repo.findById(username);
        user.get().getFollowing().add(newFollower);
        newFollower.getFollowers().add(user.get());
        return repo.save(user.get());
    }

    @Override
    public void deleteFollower(String username, Profile deletedUser) throws UserNotFoundException {
        Optional<Profile> profile = repo.findById(username);
        profile.get().getFollowers().remove(deletedUser);
        deletedUser.getFollowing().remove(profile.get());
    }

}
