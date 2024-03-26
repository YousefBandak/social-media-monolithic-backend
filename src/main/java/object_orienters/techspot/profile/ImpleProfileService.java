package object_orienters.techspot.profile;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImpleProfileService implements ProfileService {
    @Autowired
    private ProfileRepository repo;

    public ImpleProfileService(ProfileRepository repo) {
        this.repo = repo;
    }

    @Override
    public Profile getUserByUsername(String username) throws UserNotFoundException {
        return repo.findById(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public Profile createNewUser(Profile newUser) throws EmailAlreadyUsedException, UsernameAlreadyUsedExeption {
        if (repo.findByEmail(newUser.getEmail()) != null) {
            throw new EmailAlreadyUsedException(newUser.getEmail());
        }
        if (repo.findById(newUser.getUsername()) != null) {
            throw new UsernameAlreadyUsedExeption(newUser.getUsername());
        }
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

            user.setProfession(newUser.getProfession());
            user.setGender(newUser.getGender());
            user.setPublishedPosts(newUser.getPublishedPosts());
            // user.setSharedPosts(newUser.getSharedPosts());
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
    public Profile addNewFollower(String username, String followerUserName) throws UserNotFoundException {
        Profile newFollower = getUserByUsername(followerUserName);
        Optional<Profile> user = repo.findById(username);
        user.get().getFollowers().add(newFollower);
        newFollower.getFollowing().add(user.get());
        Profile savedUser = repo.save(user.get());
        repo.save(newFollower);
        return savedUser;
    }

    @Override
    public void deleteFollower(String username, Profile deletedUser) throws UserNotFoundException {
        Optional<Profile> profile = repo.findById(username);
        profile.get().getFollowers().remove(deletedUser);
        deletedUser.getFollowing().remove(profile.get());
        repo.save(profile.get());
        repo.save(deletedUser);
    }

}
