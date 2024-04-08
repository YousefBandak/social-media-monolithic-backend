package object_orienters.techspot.post;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImplSharedPostService implements SharedPostService {
    private final ProfileRepository profileRepository;
    private final SharedPostRepository sharedPostRepository;
    private final PostRepository postRepository;

    public ImplSharedPostService(ProfileRepository profileRepository, SharedPostRepository sharedPostRepository,
            PostRepository postRepository) {
        this.profileRepository = profileRepository;
        this.sharedPostRepository = sharedPostRepository;
        this.postRepository = postRepository;
    }

    public Privacy getAllowedPrincipalPrivacy(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalUsername = authentication.getName();
        return currentPrincipalUsername.equals(username) ? Privacy.PRIVATE : Privacy.PUBLIC;
    }

    @Override
    @Transactional
    public SharedPost getSharedPost(long sharedPostId) throws PostNotFoundException, ContentIsPrivateException {
        SharedPost sharedPost = sharedPostRepository.findById(sharedPostId)
                .orElseThrow(() -> new PostNotFoundException(sharedPostId));
        Privacy postPrivacy = sharedPost.getPrivacy();
        if (postPrivacy.equals(Privacy.PUBLIC)) {
            return sharedPost;
        } else if (Privacy.PRIVATE.equals(getAllowedPrincipalPrivacy(sharedPost.getSharer().getUsername()))) {
            return sharedPost;
        } else {
            throw new ContentIsPrivateException();
        }

    }

    @Override
    @Transactional
    public SharedPost createSharedPost(String sharerUsername, Long postID, String privacy)
            throws UserNotFoundException, PostNotFoundException {
        Post originalPost = postRepository.findById(postID).orElseThrow(() -> new PostNotFoundException(postID));
        Profile sharer = profileRepository.findById(sharerUsername)
                .orElseThrow(() -> new UserNotFoundException(sharerUsername));
        Privacy privacyType = Privacy.valueOf(privacy);

        SharedPost sharedPost = new SharedPost(sharer, originalPost, privacyType);
        originalPost.setNumOfShares(originalPost.getNumOfShares() + 1);
        sharedPostRepository.save(sharedPost);

        sharer.getSharedPosts().add(sharedPost);
        profileRepository.save(sharer);
        return sharedPost;
    }

    @Override
    public SharedPost updateSharedPost(long sharedPostId, Privacy newPrivacy)
            throws PostNotFoundException, ContentIsPrivateException {
        SharedPost sharedPost = getSharedPost(sharedPostId);
        sharedPost.setPrivacy(newPrivacy);
        return sharedPostRepository.save(sharedPost);
    }

    @Override
    @Transactional
    public void deleteSharedPost(String username, long sharedPostId)
            throws UserNotFoundException, PostNotFoundException, ContentIsPrivateException {
        Profile prof = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        SharedPost post = getSharedPost(sharedPostId);
        Post orignalPost = post.getPost();
        orignalPost.setNumOfShares(post.getPost().getNumOfShares() - 1);
        prof.getSharedPosts().remove(post);
        post.setPost(null);
        post.setSharer(null);
        sharedPostRepository.delete(post);
        postRepository.save(orignalPost);
        profileRepository.save(prof);
    }
}
