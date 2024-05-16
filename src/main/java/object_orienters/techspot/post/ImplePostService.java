package object_orienters.techspot.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import object_orienters.techspot.FileStorageService;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.security.model.User;
import object_orienters.techspot.security.repository.UserRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;

import object_orienters.techspot.tag.Tag;
import object_orienters.techspot.tag.TagExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import object_orienters.techspot.tag.TagRepository;

@Service
public class ImplePostService implements PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final DataTypeRepository dataTypeRepository;
    private final UserRepository userRepository;
    private final SharedPostRepository sharedPostRepository;
    private final FileStorageService fileStorageService;
    private final TagRepository tagRepository;

    private static final Pattern TAG_PATTERN = Pattern.compile("#\\w+");

    public ImplePostService(PostRepository postRepository, ProfileRepository profileRepository,
            DataTypeRepository dataTypeRepository, UserRepository userRepository,
            SharedPostRepository sharedPostRepository, FileStorageService fileStorageService, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.dataTypeRepository = dataTypeRepository;
        this.userRepository = userRepository;
        this.sharedPostRepository = sharedPostRepository;
        this.fileStorageService = fileStorageService;
        this.tagRepository = tagRepository;
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
    public Post addTimelinePosts(String username, List<MultipartFile> files,
            String text, Privacy privacy) throws UserNotFoundException, IOException {

        Profile prof = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        List<DataType> allMedia = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
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


        postRepository.save(post);

        // Extract tags and update or create them with the post ID
        Set<Tag> tags = TagExtractor.extractTags(text, post, tagName -> {
            return tagRepository.findByTagName(tagName).orElseGet(() -> {
                Tag newTag = new Tag();
                newTag.setTagName(tagName);
                return newTag;
            });
        });

        // Convert tag set to comma-separated string of tag names
        String tagsString = tags.stream().map(Tag::getTagName).collect(Collectors.joining(", "));
        post.setTags(tagsString);

        // Save updated tags
        tags.forEach(tag -> tagRepository.save(tag));

        // Save updates to the profile
        profileRepository.save(prof);

        return post;
    }










    @Override
    @Transactional
    public Post editTimelinePost(String username, long postId, List<MultipartFile> files, String text, Privacy privacy)
            throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException, IOException {

        Profile user = profileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        if ((!post.getContentAuthor().equals(user) ||
                !user.getPublishedPosts().contains(post))) {
            throw new PostUnrelatedToUserException(username, postId);
        }
        List<DataType> allMedia = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            dataTypeRepository.deleteAll(post.getMediaData());
            files.stream().forEach(file -> {
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
        post.setContentAuthor(null);
        post.getMediaData().stream().forEach(media-> {
            System.out.println("deleting medias");
            fileStorageService.deleteFile(media.getFileName());
            dataTypeRepository.delete(media);
        });
        post.setMediaData(new ArrayList<>());
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
