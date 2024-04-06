package object_orienters.techspot.post;

import java.io.IOException;
import java.util.Collection;

import object_orienters.techspot.DataTypeUtils;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImplePostService implements PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final DataTypeRepository dataTypeRepository;

    public ImplePostService(PostRepository postRepository, ProfileRepository profileRepository,
            DataTypeRepository dataTypeRepository) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.dataTypeRepository = dataTypeRepository;
    }

    public Privacy getAllowedPrincipalPrivacy(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalUsername = authentication.getName();
        return currentPrincipalUsername.equals(username) ? Privacy.PRIVATE : Privacy.PUBLIC;
    }

    @Override
    public Collection<? extends Content> getTimelinePosts(String username) throws UserNotFoundException {
        return profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username))
                .getTimelinePostsByPrivacy(getAllowedPrincipalPrivacy(username));
    }

    @Override
    public Post addTimelinePosts(String username, MultipartFile file,
            String text, Privacy privacy) throws UserNotFoundException, IOException {
        Profile user = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        DataType dataType = new DataType();
        if (file != null && !file.isEmpty()) {
            dataType.setData(DataTypeUtils.compress(file.getBytes()));
            dataType.setType(file.getContentType());
        }
        dataType.setType(dataType.getType() != null ? dataType.getType() : "text/plain");
        dataTypeRepository.save(dataType);
        Post post = new Post();
        post.setTextData(text == null ? "" : text);
        post.setPrivacy(privacy);
        post.setMediaData(dataType);
        post.setContentAuthor(user);
        user.getPublishedPosts().add(post);
        postRepository.save(post);
        profileRepository.save(user);
        return post;
    }

    // // Todo: add shared post implementation
    // public SharedPost addSharedPost(String username, Post post, Privacy privacy)
    // throws UserNotFoundException {
    // Profile user = profileRepository.findByUsername(username)
    // .orElseThrow(() -> new UserNotFoundException(username));
    // SharedPost sharedPost = new SharedPost(user, post, privacy);
    // sharedPostRepository.save(sharedPost);
    // user.getSharedPosts().add(sharedPost);
    // profileRepository.save(user);

    // return sharedPost;
    // }

    @Override
    public Post editTimelinePost(String username, long postId, MultipartFile file,
            String text, Privacy privacy)
            throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException, IOException {

        Profile user = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        if ((!post.getContentAuthor().equals(user) ||
                !user.getPublishedPosts().contains(post))) {
            throw new PostUnrelatedToUserException(username, postId);
        }
        if (file != null && !file.isEmpty()) {
            post.getMediaData().setData(DataTypeUtils.compress(file.getBytes()));
            post.getMediaData().setType(file.getContentType());
        }
        post.setPrivacy(privacy == null ? post.getPrivacy() : privacy);
        post.setTextData(text == null ? "" : text);
        // post.setAuthor(user);
        // post.setMediaData(newPost.getMediaData());
        // post.setPrivacy(newPost.getPrivacy());

        postRepository.save(post);
        // user.getPublishedPosts().add(post);
        profileRepository.save(user);
        return post;
    }

    @Override
    public void deleteTimelinePost(String username, long postId) throws UserNotFoundException, PostNotFoundException {
        Profile user = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        user.getPublishedPosts().remove(post);
        profileRepository.save(user);
        // TODO: Maybe we should mark the post for deletion instead of deleting it
        // immediately
        // TODO: Do we need to update any references to that post before deleting it?
        postRepository.delete(post);
        dataTypeRepository.delete(post.getMediaData());
    }

    @Override
    public Post getPost(long postId) throws PostNotFoundException, ContentIsPrivateException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        Privacy postPrivacy = post.getPrivacy();
        if (postPrivacy.equals(Privacy.PUBLIC))
            return post;
        else if (Privacy.PRIVATE.equals(getAllowedPrincipalPrivacy(post.getContentAuthor().getUsername()))) {
            return post;
        } else
            throw new ContentIsPrivateException();

    }

}
