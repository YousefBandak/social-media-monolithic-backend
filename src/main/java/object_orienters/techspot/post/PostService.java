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
                       SharedPostRepository sharedPostRepository, FileStorageService fileStorageService, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.dataTypeRepository = dataTypeRepository;
        this.sharedPostRepository = sharedPostRepository;
        this.fileStorageService = fileStorageService;
        this.tagRepository = tagRepository;
    }


    public List<Privacy> getAllowedPrincipalPrivacy(String username) {
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        String currentUserPrincipal = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Privacy> privacies = List.of(Privacy.PUBLIC);

        if (profile.getUsername().equals(currentUserPrincipal))
            privacies = List.of(Privacy.PRIVATE, Privacy.FRIENDS, Privacy.PUBLIC);
        else if (profile.getFollowers().contains(currentUserPrincipal))
            privacies.add(Privacy.FRIENDS);
        return privacies;
    }


    public Collection<? extends Content> getPosts(String username, int offset, int limit) throws UserNotFoundException {
        Profile user = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        return postRepository.findAllByContentAuthorAndPrivacy(user, getAllowedPrincipalPrivacy(username), PageRequest.of(offset, limit));
    }


    @Transactional
    public Post addTimelinePosts(String username, List<MultipartFile> files,
                                 String text, Privacy privacy) throws UserNotFoundException, IOException {

        Profile prof = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        List<DataType> allMedia = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            files.stream().forEach((file) -> {
                DataType media = new DataType();
                String fileName = fileStorageService.storeFile(file);
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(fileName)
                        .toUriString();
                media.setType(file.getContentType());
                media.setFileName(fileName);
                media.setFileUrl(fileDownloadUri);
                allMedia.add(media);
            });

        }

        Post post = new Post();
        post.setTextData(text != null ? text : "");
        post.setPrivacy(privacy);
        post.setMediaData(allMedia);
        post.setContentAuthor(prof);
        // prof.getPublishedPosts().add(post);
        allMedia.forEach(media -> {
            media.setContent(post);
        });
        dataTypeRepository.saveAll(allMedia);


        postRepository.save(post);

        // Extract tags and update or create them with the post ID
        Set<Tag> tags = TagExtractor.extractTags(text, post, tagName -> tagRepository.findByTagName(tagName).orElseGet(() -> {
            Tag newTag = new Tag();
            newTag.setTagName(tagName);
            return newTag;
        }));

        // Convert tag set to comma-separated string of tag names
        String tagsString = tags.stream().map(Tag::getTagName).collect(Collectors.joining(", "));
        post.setTags(tagsString);

        // Save updated tags
        tags.forEach(tag -> tagRepository.save(tag));

        // Save updates to the profile
        profileRepository.save(prof);

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
        List<DataType> allMedia = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            dataTypeRepository.deleteAll(post.getMediaData());
            files.stream().forEach(file -> {
                DataType media = new DataType();
                String fileName = fileStorageService.storeFile(file);
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(fileName)
                        .toUriString();
                media.setType(file.getContentType());
                media.setFileName(fileName);
                media.setFileUrl(fileDownloadUri);
                allMedia.add(media);
            });
        }
        post.setMediaData(allMedia);
        System.out.println(privacy);
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
//        sharedPosts.stream()
//                .map(SharedPost::getSharer)
//                .distinct()
//                .forEach(sharer -> {
//                    List<SharedPost> sharerSharedPosts = sharer.getSharedPosts();
//                    sharerSharedPosts.removeIf(sp -> sp.getPost().getContentID() == postId);
//                    profileRepository.save(sharer);
//                });
        post.setContentAuthor(null);
        post.setMediaData(new ArrayList<>());
        dataTypeRepository.deleteAll(post.getMediaData());
        //user.getPublishedPosts().remove(post);
        postRepository.delete(post);
        profileRepository.save(user);
    }


    public Post getPost(long postId) throws PostNotFoundException, ContentIsPrivateException {
        return postRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));

    }

}
