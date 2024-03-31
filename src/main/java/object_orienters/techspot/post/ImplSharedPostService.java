package object_orienters.techspot.post;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.reaction.Reaction;
import org.springframework.stereotype.Service;

@Service
public class ImplSharedPostService implements SharedPostService {
    private final ProfileRepository profileRepository;
    private final SharedPostRepository sharedPostRepository;
    private final PostRepository postRepository;

    public ImplSharedPostService(ProfileRepository profileRepository, SharedPostRepository sharedPostRepository, PostRepository postRepository) {
        this.profileRepository = profileRepository;
        this.sharedPostRepository = sharedPostRepository;
        this.postRepository = postRepository;
    }

    @Override
    public SharedPost getSharedPost(long sharedPostId) throws PostNotFoundException {
        return sharedPostRepository.findById(sharedPostId).orElseThrow(() -> new PostNotFoundException(sharedPostId));
    }

    @Override
    public SharedPost createSharedPost(String username, Long postID, String privacy)
            throws UserNotFoundException, PostNotFoundException {
        Post originalPost = postRepository.findById(postID).orElseThrow(() -> new PostNotFoundException(postID));
        Profile sharer = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        Privacy privacyType = Privacy.valueOf(privacy);

        SharedPost sharedPost = new SharedPost(sharer, originalPost, privacyType);
        sharedPostRepository.save(sharedPost);
        sharer.getSharedPosts().add(sharedPost);
        profileRepository.save(sharer);
        return sharedPost;
    }

    @Override
    public SharedPost updateSharedPost(long sharedPostId, Privacy newPrivacy) throws PostNotFoundException {
        SharedPost sharedPost = getSharedPost(sharedPostId);
        sharedPost.setPrivacy(newPrivacy);
        return sharedPostRepository.save(sharedPost);
    }

    @Override
    public void deleteSharedPost(String username, long sharedPostId)
            throws UserNotFoundException, PostNotFoundException {
        Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        SharedPost post = getSharedPost(sharedPostId);
        sharedPostRepository.delete(post);
    }
}
