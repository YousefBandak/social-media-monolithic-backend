package object_orienters.techspot.post;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.content.ContentRepository;
import object_orienters.techspot.exceptions.ContentIsPrivateException;
import object_orienters.techspot.exceptions.PostNotFoundException;
import object_orienters.techspot.exceptions.PostUnrelatedToUserException;
import object_orienters.techspot.exceptions.UserNotFoundException;
import object_orienters.techspot.model.ContentType;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.tag.TagRepository;
import object_orienters.techspot.utilities.FileStorageService;
import object_orienters.techspot.utilities.MediaDataUtilities;
import object_orienters.techspot.utilities.TagsUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final DataTypeRepository dataTypeRepository;
    private final SharedPostRepository sharedPostRepository;
    private final FileStorageService fileStorageService;
    private final TagRepository tagRepository;
    private final ContentRepository contentRepository;
    private final MediaDataUtilities mediaDataUtilities;

    TagsUtilities tagsUtilities;

    private static final Pattern TAG_PATTERN = Pattern.compile("#\\w+");

    public PostService(PostRepository postRepository,
                       ProfileRepository profileRepository,
                       DataTypeRepository dataTypeRepository,
                       SharedPostRepository sharedPostRepository,
                       FileStorageService fileStorageService,
                       TagRepository tagRepository,
                       ContentRepository contentRepository,
                       TagsUtilities tagsUtilities,
                       MediaDataUtilities mediaDataUtilities) {

        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.dataTypeRepository = dataTypeRepository;
        this.sharedPostRepository = sharedPostRepository;
        this.fileStorageService = fileStorageService;
        this.tagRepository = tagRepository;
        this.contentRepository = contentRepository;
        this.tagsUtilities = tagsUtilities;
        this.mediaDataUtilities = mediaDataUtilities;
    }


    public List<Privacy> getAllowedPrincipalPrivacy(Profile profile) {
        String currentUserPrincipal = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Privacy> privacies = List.of(Privacy.PUBLIC);

        if (profile.getUsername().equals(currentUserPrincipal))
            privacies = List.of(Privacy.PRIVATE, Privacy.FRIENDS, Privacy.PUBLIC);
        else if (profile.getFollowers().contains(currentUserPrincipal))
            privacies.add(Privacy.FRIENDS);
        return privacies;
    }


    public Page<? extends Content> getPosts(String username, int offset, int limit) throws UserNotFoundException {
        Profile user = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return contentRepository.findAllByMainAuthorAndContentTypeAndPrivacy(user, getAllowedPrincipalPrivacy(user), List.of(ContentType.Post, ContentType.SharedPost), PageRequest.of(offset, limit, Sort.by("timestamp").descending()));

    }

    @Transactional
    public Post addTimelinePosts(String username, List<MultipartFile> files,
                                 String text, Privacy privacy) throws UserNotFoundException, IOException {
        Profile prof = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        List<DataType> allMedia = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            mediaDataUtilities.handleAddMediaData(files, allMedia);
        }
        Post post = new Post();
        post.setTextData(text != null ? text : "");
        post.setPrivacy(privacy);
        post.setMediaData(allMedia);
        post.setContentAuthor(prof);
        prof.getPublishedPosts().add(post);
        allMedia.forEach(media -> {
            media.setContent(post);
        });
        dataTypeRepository.saveAll(allMedia);
        tagsUtilities.handleAddTags(text, post);
        postRepository.save(post);

        return post;
    }

    @Transactional
    public Content editTimelinePost(String username, long postId, List<MultipartFile> files, String text, Privacy privacy)
            throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException, IOException {

        Profile user = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Content content = contentRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));
        if (!content.getMainAuthor().equals(user)) {
            throw new PostUnrelatedToUserException(username, postId);
        }

        if (content instanceof Post post) {
            List<DataType> allMedia = files == null ? post.getMediaData() : new ArrayList<>();
            if (files != null && !files.isEmpty()) {
                mediaDataUtilities.handleDeleteMediaData(post);
                mediaDataUtilities.handleAddMediaData(files, allMedia);
            }
            post.setMediaData(allMedia);
            post.setPrivacy(privacy != null ? privacy : post.getPrivacy());
            post.setTextData(text != null ? text : post.getTextData());
            allMedia.forEach(media -> {
                media.setContent(post);
            });
            tagsUtilities.handleAddTags(text, post);
            dataTypeRepository.saveAll(allMedia);
            postRepository.save(post);
            profileRepository.save(user);
            return post;
        } else if (content instanceof SharedPost sharedPost) {
            sharedPost.setPrivacy(privacy != null ? privacy : sharedPost.getPrivacy());
            return sharedPostRepository.save(sharedPost);
        } else {
            return content;
        }

    }

    @Transactional
    public void deletePost(String username, long postId) throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException {
        Profile user = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Content content = contentRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));
        if (!content.getMainAuthor().equals(user)) {
            throw new PostUnrelatedToUserException(username, postId);
        }
        if (content instanceof SharedPost sharedPost) {
            sharedPost.setPost(null);
            sharedPost.setSharer(null);
            sharedPostRepository.delete(sharedPost);
        } else if (content instanceof Post post) {

            List<SharedPost> sharedPosts = sharedPostRepository.findByPost(post);
            sharedPosts.stream().forEach(sharedPost -> {
                sharedPost.setContentAuthor(null);
                sharedPost.setPost(null);
                sharedPost.setSharer(null);
                sharedPostRepository.delete(sharedPost);
            });

            post.setContentAuthor(null);
            mediaDataUtilities.handleDeleteMediaData(post);
            tagsUtilities.handleDeleteTags(post);
            post.setMediaData(new ArrayList<>());
            user.getPublishedPosts().remove(post);
            post.setComments(new ArrayList<>());
            post.setReactions(new ArrayList<>());
            postRepository.delete(post);
            profileRepository.save(user);

        }

    }

    @Transactional
    public SharedPost createSharedPost(String sharerUsername, Long postID, String privacy)
            throws UserNotFoundException, PostNotFoundException {
        Post originalPost = postRepository.findByContentID(postID).orElseThrow(() -> new PostNotFoundException(postID));
        Profile sharer = profileRepository.findById(sharerUsername)
                .orElseThrow(() -> new UserNotFoundException(sharerUsername));
        Privacy privacyType = Privacy.valueOf(privacy);

        SharedPost sharedPost = new SharedPost(sharer, originalPost, privacyType);
        originalPost.setNumOfShares(originalPost.getNumOfShares() + 1);
        sharedPostRepository.save(sharedPost);

        //sharer.getSharedPosts().add(sharedPost);
        profileRepository.save(sharer);
        return sharedPost;
    }


    public Content getPost(long postId) throws PostNotFoundException, ContentIsPrivateException {
        return contentRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));
    }

}
