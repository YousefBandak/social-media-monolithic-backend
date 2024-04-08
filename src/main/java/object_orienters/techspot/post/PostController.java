package object_orienters.techspot.post;

import jakarta.validation.Valid;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.ImpleProfileService;
import object_orienters.techspot.profile.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/profiles/{username}")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final PostModelAssembler assembler;
    private final SharedPostModelAssembler sharedPostAssembler;
    private final ImplePostService postService;
    private final ImplSharedPostService sharedPostService;

    private final ImpleProfileService profileService;

    PostController(PostModelAssembler assembler, ImplePostService postService,
            SharedPostModelAssembler sharedPostAssembler,
            ImplSharedPostService sharedPostService, ImpleProfileService profileService) {
        this.assembler = assembler;
        this.postService = postService;
        this.sharedPostAssembler = sharedPostAssembler;
        this.sharedPostService = sharedPostService;
        this.profileService = profileService;
    }

    private static String getTimestamp() {
        return LocalDateTime.now().format(formatter) + " ";
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getTimelinePosts(@PathVariable String username) {
        try {
            logger.info(">>>>Retrieving Timeline Posts... @ " + getTimestamp() + "<<<<");
            Collection<? extends Content> posts = postService.getPosts(username);
            logger.info(">>>>Timeline Posts Retrieved. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(assembler.toCollectionModel(posts));
        } catch (UserNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    @PostMapping("/posts")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<?> addTimelinePosts(@PathVariable String username,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "privacy") Privacy privacy,
            @RequestParam(value = "tags", required = false) String tags)
            throws IOException {
        try {
            logger.info(">>>>Adding Post to Timeline... @ " + getTimestamp() + "<<<<");
            List<String> tagsList = Arrays.asList(tags);
            Post profilePost = postService.addTimelinePosts(username, file, text, privacy,
                    tagsList);
            logger.info(">>>>Post Added to Timeline. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(assembler.toModel(profilePost));
        } catch (UserNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        } catch (IOException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("File Not Found").withDetail(exception.getMessage()));
        }
    }

    @PutMapping("/posts/{postId}")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<?> editTimelinePost(@PathVariable String username, @PathVariable long postId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "privacy") Privacy privacy) throws IOException { // TODO: ADD TAGS
        try {
            logger.info(">>>>Editing Post... @ " + getTimestamp() + "<<<<");
            Post editedPost = postService.editTimelinePost(username, postId, file, text, privacy);
            logger.info(">>>>Post Edited. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(assembler.toModel(editedPost));
        } catch (UserNotFoundException | PostNotFoundException | PostUnrelatedToUserException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(exception.getMessage()));
        }

    }

    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<?> deleteTimelinePost(@PathVariable String username, @PathVariable long postId) {
        try {
            logger.info(">>>>Deleting Post... @ " + getTimestamp() + "<<<<");
            postService.deletePost(username, postId);
            logger.info(">>>>Post Deleted. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException | PostNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(exception.getMessage()));
        }

    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPost(@PathVariable long postId, @PathVariable String username) {
        try {
            profileService.getUserByUsername(username);
            String aouther = postService.getPost(postId).getContentAuthor().getUsername();
            if (!Objects.equals(username, aouther)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Problem.create().withTitle("Not Found").withDetail(
                                "User: " + username + " is not the author of this content." + "Author is: " + aouther));
            }
            logger.info(">>>>Retrieving Post... @ " + getTimestamp() + "<<<<");
            Post post = postService.getPost(postId);
            logger.info(">>>>Post Retrieved. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(assembler.toModel(post));
        } catch (PostNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Post Not Found").withDetail(exception.getMessage()));
        } catch (ContentIsPrivateException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Problem.create().withTitle("Action Not Allowed").withDetail(exception.getMessage()));
        } catch (UserNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }

    }

    // @GetMapping("/posts/{postId}/media")
    // public ResponseEntity<?> getPostMedia(@PathVariable long postId) {
    // try {
    // Post post = postService.getPost(postId);
    // if (post.getMediaData().getType().equalsIgnoreCase("image/jpeg")) {
    // byte[] Bytes =
    // DataTypeUtils.safelyDecompress(post.getMediaData().getType().getBytes());
    // logger.info(">>>>Post Retrieved. @ " + getTimestamp() + "<<<<"); // its not
    // getting decompressed
    // return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
    // .body(Bytes);
    // }
    // } catch (PostNotFoundException exception) {
    // logger.info(">>>>Error Occurred: " + exception.getMessage() + " @ " +
    // getTimestamp() + "<<<<");
    // return ResponseEntity.status(HttpStatus.NOT_FOUND)
    // .body(Problem.create().withTitle("Post Not
    // Found").withDetail(exception.getMessage()));
    // } catch (ContentIsPrivateException exception) {
    // logger.info(">>>>Error Occurred: " + exception.getMessage() + " @ " +
    // getTimestamp() + "<<<<");
    // return ResponseEntity.status(HttpStatus.FORBIDDEN)
    // .body(Problem.create().withTitle("Action Not
    // Allowed").withDetail(exception.getMessage()));
    // }
    // return null;
    // }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping("/posts/{postId}/share")
    // @PreAuthorize("#bodyMap.get(\"sharer\") ==
    // authentication.principal.username")
    @PreAuthorize("#bodyMap['sharer'] == authentication.principal.username")
    public ResponseEntity<?> createSharePost(@PathVariable String username, @PathVariable Long postId,
            @RequestBody Map<String, String> bodyMap) {
        try {
            logger.info(">>>>Sharing Post... @ " + getTimestamp() + "<<<<");
            SharedPost sharedPost = sharedPostService.createSharedPost(bodyMap.get("sharer"), postId,
                    bodyMap.get("privacy"));
            logger.info(">>>>Post Shared. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.CREATED).body(sharedPostAssembler.toModel(sharedPost));
        } catch (PostNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            exception = new PostNotFoundException(postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Post Not Found").withDetail(exception.getMessage()));
        } catch (UserNotFoundException exception) {
            logger.info(">>>>Error Occurred: " + exception.getMessage() + " @ " +
                    getTimestamp() + "<<<<");
            exception = new UserNotFoundException(username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }

    }

    @GetMapping("/sharedPosts/{postId}")
    public ResponseEntity<?> getSharedPost(@PathVariable long postId, @PathVariable String username) {
        try {
            profileService.getUserByUsername(username);
            String aouther = postService.getPost(postId).getContentAuthor().getUsername();
            if (!Objects.equals(username, aouther)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Problem.create().withTitle("Not Found").withDetail(
                                "User: " + username + " is not the author of this content." + "Author is: " + aouther));
            }
            logger.info(">>>>Retrieving Shared Post... @ " + getTimestamp() + "<<<<");
            SharedPost sharedPost = sharedPostService.getSharedPost(postId);
            logger.info(">>>>Sharing Post Retrieved. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(sharedPostAssembler.toModel(sharedPost));
        } catch (PostNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            exception = new PostNotFoundException(postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle(" Post Not Found").withDetail(exception.getMessage()));
        } catch (UserNotFoundException exception) {
            logger.info(">>>>Error Occurred: " + exception.getMessage() + " @ " +
                    getTimestamp() + "<<<<");
            exception = new UserNotFoundException(username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        } catch (ContentIsPrivateException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Problem.create().withTitle("Action Not Allowed").withDetail(exception.getMessage()));
        }

    }

    @DeleteMapping("/sharedPosts/{postId}")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<?> deleteSharedPost(@PathVariable String username, @PathVariable Long postId) {
        try {
            logger.info(">>>>Deleting Shared Post... @ " + getTimestamp() + "<<<<");
            sharedPostService.deleteSharedPost(username, postId);
            logger.info(">>>>Shared Post Deleted. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.noContent().build();
        } catch (PostNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            exception = new PostNotFoundException(postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Post Not Found").withDetail(exception.getMessage()));
        } catch (UserNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            exception = new UserNotFoundException(username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        } catch (ContentIsPrivateException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Problem.create().withTitle("Action Not Allowed").withDetail(exception.getMessage()));
        }
    }

    @PutMapping("/sharedPosts/{postId}")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<?> updateSharedPost(@PathVariable String username, @PathVariable Long postId,
            @Valid @RequestBody Map<String, String> bodyMap) {
        try {
            logger.info(">>>>Editing Shared Post... @ " + getTimestamp() + "<<<<");
            SharedPost updatedSharedPost = sharedPostService.updateSharedPost(postId,
                    Privacy.valueOf(bodyMap.get("privacy")));
            EntityModel<SharedPost> sharedPostModel = sharedPostAssembler.toModel(updatedSharedPost);
            logger.info(">>>>Shared Post Edited.  @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(sharedPostModel);
        } catch (PostNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            exception = new PostNotFoundException(postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Post Not Found").withDetail(exception.getMessage()));
        } catch (ContentIsPrivateException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Problem.create().withTitle("Action Not Allowed").withDetail(exception.getMessage()));
        }
    }
}
