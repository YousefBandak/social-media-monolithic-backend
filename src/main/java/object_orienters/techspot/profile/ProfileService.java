package object_orienters.techspot.profile;

import object_orienters.techspot.exceptions.ProfileNotFoundException;
import object_orienters.techspot.exceptions.UserCannotFollowSelfException;
import object_orienters.techspot.exceptions.UserNotFoundException;
import object_orienters.techspot.message.Chatter;
import object_orienters.techspot.message.ChatterService;
import object_orienters.techspot.message.Status;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import object_orienters.techspot.security.repository.UserRepository;
import object_orienters.techspot.utilities.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataTypeRepository dataTypeRepository;

    @Autowired
    ChatterService chatterService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    PasswordEncoder encoder;

    public ProfileService(ProfileRepository repo) {
        this.repo = repo;
    }

    public Profile getUserByUsername(String username) throws UserNotFoundException {
        return repo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public Profile createNewProfile(String username, String email, String name, MultipartFile file) throws IOException {
        Profile newProfile = new Profile();
        DataType profilePic = new DataType();
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().equals("image/jpeg") || !file.getContentType().equals("image/png")) {
                throw new IllegalArgumentException("Unsupported file type. Please upload a JPEG or PNG image.");
            }
            String fileName = fileStorageService.storeFile(file);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/media_uploads/")
                    .path(fileName)
                    .toUriString();
            profilePic.setType(file.getContentType());
            profilePic.setFileName(fileName);
            profilePic.setFileUrl(fileDownloadUri);
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

    @Transactional
    public Profile updateUserProfile(UpdateProfile newUser, String username) throws UserNotFoundException {
        Profile updatedUser = repo.findByUsername(username).map(user -> {
            user.setDob(newUser.getDob());
            user.setEmail(newUser.getEmail());
            user.getOwner().setEmail(newUser.getEmail());
            user.setName(newUser.getName());
            user.setProfession(newUser.getProfession());
            user.setGender(newUser.getGender());
            user.getOwner().setPassword(encoder.encode(newUser.getPassword()));
            return repo.save(user);
        }).orElseThrow(() -> new UserNotFoundException(username));
        return updatedUser;
    }

    public Optional<Profile> getFollowingByUsername(String username, String followingUsername)
            throws UserNotFoundException {
        return repo.findFollowingByUsername(username, followingUsername);
    }

    public Profile getFollowerByUsername(String username, String followerUserName) throws UserNotFoundException {
        return repo.findFollowerByUsername(username, followerUserName)
                .orElseThrow(() -> new UserNotFoundException(followerUserName));
    }

    public Page<Profile> getUserFollowersByUsername(String username, int page, int size) throws UserNotFoundException {
        return repo.findFollowersByUserId(username, PageRequest.of(page, size));
    }

    public Page<Profile> getUserFollowingByUsername(String username, int page, int size) throws UserNotFoundException {
        return repo.findFollowingByUserId(username, PageRequest.of(page, size));
    }

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

    public void deleteFollower(String username, String deletedUser) throws UserNotFoundException {
        Profile profile = repo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        Profile deletedProfile = repo.findByUsername(deletedUser)
                .orElseThrow(() -> new UserNotFoundException(deletedUser));
        profile.getFollowers().remove(deletedProfile);
        deletedProfile.getFollowing().remove(profile);
        repo.save(profile);
        repo.save(deletedProfile);
    }

    public void deleteFollowing(String username, String deletedUser) throws UserNotFoundException {
        Optional<Profile> profile = repo.findByUsername(username);
        Optional<Profile> deletedProfile = repo.findByUsername(deletedUser);
        profile.get().getFollowing().remove(deletedProfile.get());
        deletedProfile.get().getFollowers().remove(profile.get());
        repo.save(profile.get());
        repo.save(deletedProfile.get());
    }

    @Transactional
    public Profile addProfilePic(String username, MultipartFile file, String text)
            throws UserNotFoundException, IOException {
        Profile user = repo.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        DataType profilePic = new DataType();
        if (file != null && !file.isEmpty()) {
            // if (!file.getContentType().equals("image/jpeg") || !file.getContentType().equals("image/png")) {
            //     throw new IllegalArgumentException("Unsupported file type. Please upload a JPEG or PNG image.");
            // }
            String fileName = fileStorageService.storeFile(file);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/media_uploads/")
                    .path(fileName)
                    .toUriString();
            profilePic.setType(file.getContentType());
            profilePic.setFileName(fileName);
            profilePic.setFileUrl(fileDownloadUri);
        }
        user.setProfilePic(profilePic);
        dataTypeRepository.save(profilePic);
        return repo.save(user);
    }

    // public void deleteProfile(String username) throws UserNotFoundException {
    // repo.delete(repo.findById(username).get());
    // }

}
