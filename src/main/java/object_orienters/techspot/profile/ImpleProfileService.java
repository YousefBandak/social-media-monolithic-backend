package object_orienters.techspot.profile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import object_orienters.techspot.message.Chatter;
import object_orienters.techspot.message.ChatterService;
import object_orienters.techspot.message.Status;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import object_orienters.techspot.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImpleProfileService implements ProfileService {
    @Autowired
    private ProfileRepository repo;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DataTypeRepository dataTypeRepository;

    @Autowired
    ChatterService chatterService;

    Logger log = LoggerFactory.getLogger(ImpleProfileService.class.getName());

    public ImpleProfileService(ProfileRepository repo) {
        this.repo = repo;
    }

    @Override
    public Profile getUserByUsername(String username) throws UserNotFoundException {
        return repo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public Profile createNewProfile(String username, String email, String name, MultipartFile file) throws IOException {
        Profile newProfile = new Profile();
        DataType profilePic = new DataType();
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().equals("image/jpeg") || !file.getContentType().equals("image/png")) {
                throw new IllegalArgumentException("Unsupported file type. Please upload a JPEG or PNG image.");
            }
            profilePic.setData(file.getBytes());
            profilePic.setType(file.getContentType());
        }
        newProfile.setProfilePic(profilePic);
        dataTypeRepository.save(profilePic);
        newProfile.setProfilePic(profilePic);
        newProfile.setUsername(username);
        newProfile.setOwner(
                userRepository.findByUsername(username).orElseThrow(() -> new ProfileNotFoundException(username)));
        newProfile.setEmail(email);
        newProfile.setName(name);
        chatterService.saveChatter(new Chatter(newProfile.getUsername(), newProfile.getName(), Status.ONLINE));
        return repo.save(newProfile);

    }

    @Override
    @Transactional
    public Profile updateUserProfile(Profile newUser, String username) throws UserNotFoundException {
        Profile updatedUser = repo.findByUsername(username).map(user -> {
            // user.getOwner.(newUser.getUsername()); //NOTE: user cannot change username
            // user.setProfilePic(newUser.getProfilePic());
            user.setDob(newUser.getDob());
            user.setEmail(newUser.getEmail());
            user.getOwner().setEmail(newUser.getEmail());
            user.setFollowers(newUser.getFollowers());
            user.setFollowing(newUser.getFollowing());
            user.setName(newUser.getName());
            // user.setUsername(newUser.getUsername());
            // user.getOwner().setUsername(newUser.getUsername());
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
        if (username.equals(followerUserName)) {
            throw new UserCannotFollowSelfException(followerUserName);
        }
        Profile newFollower = getUserByUsername(followerUserName);
        Optional<Profile> user = repo.findByUsername(username);
        user.get().getFollowers().add(newFollower);
        newFollower.getFollowing().add(user.get());
        Profile savedUser = repo.save(user.get());
        repo.save(newFollower);
        return savedUser;
    }

    @Override
    public void deleteFollower(String username, String deletedUser) throws UserNotFoundException {
        Optional<Profile> profile = repo.findById(username);
        Optional<Profile> deletedProfile = repo.findByUsername(deletedUser);
        profile.get().getFollowers().remove(deletedProfile.get());
        deletedProfile.get().getFollowing().remove(profile.get());
        repo.save(profile.get());
        repo.save(deletedProfile.get());
    }

    public void deleteFollowing(String username, String deletedUser) throws UserNotFoundException {
        Optional<Profile> profile = repo.findByUsername(username);
        Optional<Profile> deletedProfile = repo.findByUsername(deletedUser);
        profile.get().getFollowing().remove(deletedProfile.get());
        deletedProfile.get().getFollowers().remove(profile.get());
        repo.save(profile.get());
        repo.save(deletedProfile.get());
    }

    @Override
    public Profile addProfilePic(String username, MultipartFile file, String text) // TODO: theres a problem
            throws UserNotFoundException, IOException {
        Profile user = repo.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        DataType profilePic = new DataType();
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().equalsIgnoreCase("image/jpeg") ||
                    !file.getContentType().equals("image/png")) {
                throw new IllegalArgumentException("Unsupported file type. Please upload a JPEG or PNG image.");
            }
            profilePic.setData(file.getBytes());
            profilePic.setType(file.getContentType());
        }
        user.setProfilePic(profilePic);
        dataTypeRepository.save(profilePic);
        return repo.save(user);
    }

    @Override
    public void deleteProfile(String username) throws UserNotFoundException {
        repo.delete(repo.findById(username).get());
    }

}
