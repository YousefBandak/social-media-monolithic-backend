package object_orienters.techspot.profile;

import java.util.List;
import java.util.Optional;

import object_orienters.techspot.security.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImpleProfileService implements ProfileService {
    @Autowired
    private ProfileRepository repo;

    @Autowired
    private UserRepository userRepository;

    Logger log = LoggerFactory.getLogger(ImpleProfileService.class.getName());

    public ImpleProfileService(ProfileRepository repo) {
        this.repo = repo;
    }

    @Override
    public Profile getUserByUsername(String username) throws UserNotFoundException {
        return repo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public Profile createNewProfile(Profile newProfile) throws EmailAlreadyUsedException, UsernameAlreadyUsedExeption {
        if (repo.findByEmail(newProfile.getEmail()) != null) {
            throw new EmailAlreadyUsedException(newProfile.getEmail());
        }
        if (repo.findByUsername(newProfile.getUsername()) != null) {
            throw new UsernameAlreadyUsedExeption(newProfile.getUsername());
        }
        return repo.save(newProfile);
    }

    @Override
    public Profile createNewProfile(String username, String email, String name) {
        Profile newProfile = new Profile();
        newProfile.setOwner(userRepository.findByUsername(username).orElseThrow(() -> new ProfileNotFoundException(username)));
        newProfile.setEmail(email);
        newProfile.setName(name);
        return repo.save(newProfile);

    }


    @Override
    public Profile updateUserProfile(Profile newUser, String username) throws UserNotFoundException {
        Profile updatedUser = repo.findByUsername(username).map(user -> {
            //user.getOwner.(newUser.getUsername()); //NOTE: user cannot change username
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
        Optional<Profile> user = repo.findByUsername(username);
        user.get().getFollowers().add(newFollower);
        newFollower.getFollowing().add(user.get());
        Profile savedUser = repo.save(user.get());
        repo.save(newFollower);
        return savedUser;
    }

    @Override
    public void deleteFollower(String username, Profile deletedUser) throws UserNotFoundException {
        Optional<Profile> profile = repo.findByUsername(username);
        profile.get().getFollowers().remove(deletedUser);
        deletedUser.getFollowing().remove(profile.get());
        repo.save(profile.get());
        repo.save(deletedUser);
    }

}
