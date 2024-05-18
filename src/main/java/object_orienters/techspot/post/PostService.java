package object_orienters.techspot.post;

import object_orienters.techspot.FileStorageService;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.security.repository.UserRepository;
import object_orienters.techspot.tag.Tag;
import object_orienters.techspot.tag.TagExtractor;
import object_orienters.techspot.tag.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final DataTypeRepository dataTypeRepository;
    private final SharedPostRepository sharedPostRepository;
    private final FileStorageService fileStorageService;
    private final TagRepository tagRepository;

    private static final Pattern TAG_PATTERN = Pattern.compile("#\\w+");

    public PostService(PostRepository postRepository, ProfileRepository profileRepository,
            DataTypeRepository dataTypeRepository, UserRepository userRepository,
            SharedPostRepository sharedPostRepository, FileStorageService fileStorageService,
            TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.dataTypeRepository = dataTypeRepository;
        this.sharedPostRepository = sharedPostRepository;
        this.fileStorageService = fileStorageService;
        this.tagRepository = tagRepository;
    }

    public List<Privacy> getAllowedPrincipalPrivacy(String username) {
        Profile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        String currentUserPrincipal = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Privacy> privacies = List.of(Privacy.PUBLIC);

        if (profile.getUsername().equals(currentUserPrincipal))
            privacies = List.of(Privacy.PRIVATE, Privacy.FRIENDS, Privacy.PUBLIC);
        else if (profile.getFollowers().contains(currentUserPrincipal))
            privacies.add(Privacy.FRIENDS);
        return privacies;
    }

    public Collection<? extends Content> getPosts(String username, int offset, int limit) throws UserNotFoundException {
        Profile user = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return postRepository.findAllByContentAuthorAndPrivacy(user, getAllowedPrincipalPrivacy(username),
                PageRequest.of(offset, limit));
    }

    @Transactional
    public Post addTimelinePosts(String username, List<MultipartFile> files,
            String text, Privacy privacy) throws UserNotFoundException, IOException {
        Profile prof = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        List<DataType> allMedia = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            handleAddMediaData(files, allMedia);
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
        handleAddTags(text, post);
        postRepository.save(post);

        return post;
    }

    @Transactional
    public Post editTimelinePost(String username, long postId, List<MultipartFile> files, String text, Privacy privacy)
            throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException, IOException {

        Profile user = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));
        if (!post.getContentAuthor().equals(user)) {
            throw new PostUnrelatedToUserException(username, postId);
        }
        List<DataType> allMedia = files == null ? post.getMediaData() : new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            handleDeleteMediaData(post);
            handleAddMediaData(files, allMedia);
        }
        post.setMediaData(allMedia);
        post.setPrivacy(privacy != null ? privacy : post.getPrivacy());
        System.out.println(text);
        post.setTextData(text != null ? text : post.getTextData());
        allMedia.forEach(media -> {
            media.setContent(post);
        });
        dataTypeRepository.saveAll(allMedia);
        postRepository.save(post);
        profileRepository.save(user);
        return post;
    }

    @Transactional
    public void deletePost(String username, long postId) throws UserNotFoundException, PostNotFoundException {
        Profile user = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));
        List<SharedPost> sharedPosts = sharedPostRepository.findByPost(post);
        // sharedPosts.stream()
        // .map(SharedPost::getSharer)
        // .distinct()
        // .forEach(sharer -> {
        // List<SharedPost> sharerSharedPosts = sharer.getSharedPosts();
        // sharerSharedPosts.removeIf(sp -> sp.getPost().getContentID() == postId);
        // profileRepository.save(sharer);
        // });
        post.setContentAuthor(null);
        handleDeleteMediaData(post);
        handleDeleteTags(post);
        post.setMediaData(new ArrayList<>());
        user.getPublishedPosts().remove(post);
        postRepository.delete(post);
        profileRepository.save(user);
    }

    public Post getPost(long postId) throws PostNotFoundException, ContentIsPrivateException {
        return postRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));

    }

    private void handleDeleteTags(Post post) {
        String postTags = post.getTags();
        String[] tagsArray = postTags.split(",");
        List<String> tagsList = new ArrayList<>(Arrays.asList(tagsArray));
        tagsList.stream().forEach(tagString -> {
            Tag tag = tagRepository.findById(tagString).get();
            String posts = tag.getPosts();
            String[] postsArray = posts.split(",");
            List<String> postList = new ArrayList<>(Arrays.asList(postsArray));
            if (postList.size() == 1) {
                tagRepository.delete(tag);
            } else {
                postList.remove(Long.toString(post.getContentID()));
                tag.setPosts(String.join(",", postList));
                tagRepository.save(tag);
            }
        });
    }

    private void handleAddTags(String text, Post post) {
        Set<Tag> newTags = TagExtractor.extractTags(text, post, tagName -> {
            return tagRepository.findByTagName(tagName).orElseGet(() -> {
                Tag newTag = new Tag();
                newTag.setTagName(tagName);
                return newTag;
            });
        });
        String tagsString = newTags.stream().map(Tag::getTagName).collect(Collectors.joining(", "));
        post.setTags(tagsString);
        newTags.forEach(tag -> tagRepository.save(tag));
    }

    private void handleAddMediaData(List<MultipartFile> files, List<DataType> allMedia) {
        files.stream().forEach((file) -> {
            DataType media = new DataType();
            String fileName = fileStorageService.storeFile(file);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/media_uploads/")
                    .path(fileName)
                    .toUriString();
            media.setType(file.getContentType());
            media.setFileName(fileName);
            media.setFileUrl(fileDownloadUri);
            allMedia.add(media);
        });
    }

    private void handleDeleteMediaData(Post post) {
        post.getMediaData().stream().forEach(media -> {
            fileStorageService.deleteFile(media.getFileName());
            dataTypeRepository.delete(media);
        });
    }

}
