package object_orienters.techspot.post;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import object_orienters.techspot.DataTypeUtils;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.security.model.User;
import object_orienters.techspot.security.repository.UserRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImplePostService implements PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final DataTypeRepository dataTypeRepository;
    private final UserRepository userRepository;
    private final SharedPostRepository sharedPostRepository;

    public ImplePostService(PostRepository postRepository, ProfileRepository profileRepository,
            DataTypeRepository dataTypeRepository, UserRepository userRepository,
            SharedPostRepository sharedPostRepository) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.dataTypeRepository = dataTypeRepository;
        this.userRepository = userRepository;
        this.sharedPostRepository = sharedPostRepository;
    }

    public Privacy getAllowedPrincipalPrivacy(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalUsername = authentication.getName();
        return currentPrincipalUsername.equals(username) ? Privacy.PRIVATE : Privacy.PUBLIC;
    }

    @Override
    public Collection<? extends Content> getPosts(String username) throws UserNotFoundException {
        return profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username))
                .getTimelinePostsByPrivacy(getAllowedPrincipalPrivacy(username));
    }

    @Override
    @Transactional
    public Post addTimelinePosts(String username, MultipartFile file,
            String text, Privacy privacy, List<String> tags) throws UserNotFoundException, IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Profile prof = profileRepository.findByOwner(user)
                .orElseThrow(() -> new UserNotFoundException(username));
        DataType dataType = new DataType();
        if (file != null && !file.isEmpty()) {
            dataType.setData(DataTypeUtils.compress(file.getBytes()));
            dataType.setType(file.getContentType());
        }
        dataType.setType(dataType.getType() != null ? dataType.getType() : "text/plain");
        dataType.setData(dataType.getData() != null ? dataType.getData() : new byte[10]);
        dataTypeRepository.save(dataType);
        Post post = new Post();
        post.setTags(tags);
        post.setTextData(text != null ? text : "");
        post.setPrivacy(privacy);
        post.setMediaData(dataType);
        post.setContentAuthor(prof);
        prof.getPublishedPosts().add(post);
        postRepository.save(post);
        profileRepository.save(prof);
        return post;
    }

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
        // post.setTextData(newPost.getTextData());
        postRepository.save(post);
        // user.getPublishedPosts().add(post);
        profileRepository.save(user);
        return post;
    }

    @Override
    @Transactional
    public void deletePost(String username, long postId) throws UserNotFoundException, PostNotFoundException {
        Profile user = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        List<SharedPost> sharedPosts = sharedPostRepository.findByPost(post);
        sharedPosts.stream()
                .map(SharedPost::getSharer)
                .distinct()
                .forEach(sharer -> {
                    List<SharedPost> sharerSharedPosts = sharer.getSharedPosts();
                    sharerSharedPosts.removeIf(sp -> sp.getPost().getContentID() == postId);
                    profileRepository.save(sharer);
                });
        DataType mediaData = post.getMediaData();
        post.setContentAuthor(null);
        post.setMediaData(null);
        dataTypeRepository.delete(mediaData);
        user.getPublishedPosts().remove(post);
        postRepository.delete(post);
        profileRepository.save(user);
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
