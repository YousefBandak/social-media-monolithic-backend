package object_orienters.techspot.post;

import java.util.Collection;

import jakarta.persistence.Entity;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ImplePostService implements PostService {
    private PostRepository postRepository;
    private ProfileRepository profileRepository;

    public ImplePostService(PostRepository postRepository, ProfileRepository profileRepository) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public Collection<Post> getTimelinePosts(String username) throws UserNotFoundException {
        return postRepository.findByAuthor(
                profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username)));
    }

    @Override
    public Post addTimelinePosts(String username, Post post) throws UserNotFoundException {
        Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        post.setAuthor(user);
        postRepository.save(post);

        // TODO: Specify if post is shared or authored
        user.getPublishedPosts().add(post);
        profileRepository.save(user);
        return post;
    } 

    @Override
    public Post editTimelinePost(String username, long postId, Post newPost)
            throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException {
        Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        if ((!post.getAuthor().equals(user) || !user.getPublishedPosts().contains(post))
                && !user.getSharedPosts().contains(post)) {
            throw new PostUnrelatedToUserException(username, postId);
        }

        // TODO: make sure the post has the same author as the user, otherwise anyone
        // can edit any post and make themselves the author

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
        Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        // TODO: Maybe we should mark the post for deletion instead of deleting it
        // immediately
        // TODO: Do we need to update any references to that post before deleting it?
        postRepository.delete(post);
    }

    @Override
    public Post getPost(long postId) throws PostNotFoundException {
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
    }

}
