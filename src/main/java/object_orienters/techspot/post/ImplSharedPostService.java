package object_orienters.techspot.post;

import java.util.Optional;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;

public class ImplSharedPostService implements SharedPostService {
    private final ProfileRepository profileRepository;
    private final SharedPostRepository sharedPostRepository;

    public ImplSharedPostService(ProfileRepository profileRepository, SharedPostRepository sharedPostRepository) {
        this.profileRepository = profileRepository;
        this.sharedPostRepository = sharedPostRepository;
    }

    @Override
    public SharedPost getSharedPost(long sharedPostId) throws UserNotFoundException, PostNotFoundException {
        return sharedPostRepository.findById(sharedPostId).orElseThrow(() -> new PostNotFoundException(sharedPostId));
    }

    @Override
    public SharedPost addSharedPost(String username, Post post, Privacy privacy)
            throws UserNotFoundException, PostNotFoundException {
        Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        SharedPost sharedPost = new SharedPost(user, post, privacy);
        sharedPostRepository.save(sharedPost);
        user.getSharedPosts().add(sharedPost);
        profileRepository.save(user);
        return sharedPost;
    }

    @Override
    public SharedPost editSharedPost(String username, long sharedPostId, Privacy newPrivacy)
            throws UserNotFoundException, PostNotFoundException {
        Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        SharedPost sharedPost = getSharedPost(sharedPostId);
        sharedPost.setPrivacy(newPrivacy);
        return sharedPostRepository.save(sharedPost);
    }

    @Override
    public void deleteSharedPost(String username, long sharedPostId)
            throws UserNotFoundException, PostNotFoundException {
        Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        sharedPostRepository.delete(getSharedPost(sharedPostId));
    }
}
