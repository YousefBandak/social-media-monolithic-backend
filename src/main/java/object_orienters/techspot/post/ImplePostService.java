package object_orienters.techspot.post;

import java.util.Collection;


import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ImplePostService implements PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final SharedPostRepository sharedPostRepository;

    private final Logger logger = LoggerFactory.getLogger(ImplePostService.class);

    public ImplePostService(PostRepository postRepository, ProfileRepository profileRepository, SharedPostRepository sharedPostRepository) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.sharedPostRepository = sharedPostRepository;
    }

    public Privacy getAllowedPrincipalPrivacy(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalUsername = authentication.getName();
        return currentPrincipalUsername.equals(username) ? Privacy.PRIVATE : Privacy.PUBLIC;
    }
    @Override
    public Collection<? extends Content> getTimelinePosts(String username) throws UserNotFoundException {
        return profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username)).getTimelinePostsByPrivacy(getAllowedPrincipalPrivacy(username));
    }

    @Override
    public Post addTimelinePosts(String username, Post post) throws UserNotFoundException {
        Profile user = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        post.setContentAuthor(user);
        user.getPublishedPosts().add(post);
        postRepository.save(post);
        profileRepository.save(user);
        return post;
    }

    //Todo: add shared post implementation
    public SharedPost addSharedPost(String username, Post post, Privacy privacy) throws UserNotFoundException {
        Profile user = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        SharedPost sharedPost = new SharedPost(user, post, privacy);
        sharedPostRepository.save(sharedPost);
        user.getSharedPosts().add(sharedPost);
        profileRepository.save(user);


        return sharedPost;
    }

    @Override
    public Post editTimelinePost(String username, long postId, Post newPost)
            throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException {

        Profile user = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        if ((!post.getContentAuthor().equals(user) ||
                !user.getPublishedPosts().contains(post))
//                && user.getSharedPosts().stream().map(SharedPost::getPost).noneMatch(e -> e.equals(post))
               ) {
            throw new PostUnrelatedToUserException(username, postId);
        }


        // post.setAuthor(user);
        post.setContent(newPost.getContent());
        post.setPrivacy(newPost.getPrivacy());

        postRepository.save(post);

        // TODO: Specify if post is shared or authored
        user.getPublishedPosts().add(post);
        profileRepository.save(user);
        return post;
    }

    @Override
    public void deleteTimelinePost(String username, long postId) throws UserNotFoundException, PostNotFoundException {
        Profile user = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        // TODO: Maybe we should mark the post for deletion instead of deleting it
        // immediately
        // TODO: Do we need to update any references to that post before deleting it?
        postRepository.delete(post);
    }

    @Override
    public Post getPost(long postId) throws PostNotFoundException, ContentIsPrivateException {

       Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
       Privacy postPrivacy = post.getPrivacy();
       if (postPrivacy.equals(Privacy.PUBLIC))
           return post;

       else if (Privacy.PRIVATE.equals(getAllowedPrincipalPrivacy(post.getContentAuthor().getUsername()))){
           return post;
       }else
           throw new ContentIsPrivateException();

    }

}
