package object_orienters.techspot.post;

import jakarta.validation.Valid;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.ProfileService;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.security.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles/{username}")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final PostModelAssembler assembler;
    private final SharedPostModelAssembler sharedPostAssembler;
    private final PostService postService;
    private final ProfileService profileService;

    private final PermissionService permissionService;

    PostController(PostModelAssembler assembler,
                   PostService postService,
                   SharedPostModelAssembler sharedPostAssembler,
                   ProfileService profileService,
                   PermissionService permissionService) {
        this.assembler = assembler;
        this.postService = postService;
        this.sharedPostAssembler = sharedPostAssembler;
        this.profileService = profileService;
        this.permissionService = permissionService;
    }

    private static String getTimestamp() {
        return LocalDateTime.now().format(formatter) + " ";
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getTimelinePosts(@PathVariable String username, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit) {
        try {
            logger.info(">>>>Retrieving Timeline Posts... @ " + getTimestamp() + "<<<<");

            Page<? extends Content> posts =  postService.getPosts(username, offset, limit);
            PagedModel<EntityModel<? extends Content>> pagedModel = PagedModel.of(posts.stream().map(assembler::toModel).collect(Collectors.toList()),
                    new PagedModel.PageMetadata(posts.getSize(), posts.getNumber(), posts.getTotalElements(), posts.getTotalPages()));

            logger.info(">>>>Timeline Posts Retrieved. @ " + getTimestamp() + "<<<<");

            return ResponseEntity.ok(pagedModel);
        } catch (UserNotFoundException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    @PostMapping("/posts")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<?> addTimelinePosts(@PathVariable String username,
                                              @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                              @RequestParam(value = "text", required = false) String text,
                                              @RequestParam(value = "privacy") Privacy privacy)
            throws IOException {
        try {
            logger.info(">>>>Adding Post to Timeline... @ " + getTimestamp() + "<<<<");
            Post profilePost = postService.addTimelinePosts(username, files, text, privacy);
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
                                              @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                              @RequestParam(value = "text", required = false) String text,
                                              @RequestParam(value = "privacy") Privacy privacy) throws IOException {
        try {
            logger.info(">>>>Editing Post... @ " + getTimestamp() + "<<<<");
            Content editedPost = postService.editTimelinePost(username, postId, files, text, privacy);
            logger.info(">>>>Post Edited. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(assembler.toModel(editedPost));
        } catch (UserNotFoundException | PostNotFoundException | PostUnrelatedToUserException exception) {
            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(exception.getMessage()));
        }

    }

    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("#username == authentication.principal.username && @permissionService.canAccessPost(#postId, #username)")
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
        } catch (PostUnrelatedToUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Problem.create().withTitle("Action Not Allowed").withDetail(e.getMessage()));
        }

    }

    @GetMapping("/posts/{postId}")
    @PreAuthorize("@permissionService.canAccessPost(#postId, #username)")
    public ResponseEntity<?> getPost(@PathVariable long postId, @PathVariable String username) {
        try {
            profileService.getUserByUsername(username);
            logger.info(">>>>Retrieving Post... @ " + getTimestamp() + "<<<<");
            Content post = postService.getPost(postId);
            String aouther = post.getMainAuthor().getUsername();
            if (!Objects.equals(username, aouther)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Problem.create().withTitle("Not Found").withDetail(
                                "User: " + username + " is not the author of this content." + "Author is: " + aouther));
            }

            logger.info(">>>>Post Retrieved. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(post);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping("/posts/{postId}/share")
    @PreAuthorize("#bodyMap['sharer'] == authentication.principal.username && @permissionService.isPostPublic(#postId)")
    public ResponseEntity<?> createSharePost(@PathVariable String username, @PathVariable Long postId,
                                             @RequestBody Map<String, String> bodyMap) {
        try {
            logger.info(">>>>Sharing Post... @ " + getTimestamp() + "<<<<");
            SharedPost sharedPost = postService.createSharedPost(bodyMap.get("sharer"), postId,
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

//    //NOTE TO BE REMOVED
//    @GetMapping("/sharedPosts/{postId}")
//    public ResponseEntity<?> getSharedPost(@PathVariable long postId, @PathVariable String username) {
//        try {
//            profileService.getUserByUsername(username);
//            logger.info(">>>>Retrieving Shared Post... @ " + getTimestamp() + "<<<<");
//            SharedPost sharedPost = sharedPostService.getSharedPost(postId);
//            String author = sharedPost.getSharer().getUsername();
//            if (!Objects.equals(username, author) && sharedPost.getPrivacy() != Privacy.PRIVATE) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(Problem.create().withTitle("Not Found").withDetail(
//                                "User: " + username + " is not the author of this content." + "Author is: " + author));
//            }
//            logger.info(">>>>Sharing Post Retrieved. @ " + getTimestamp() + "<<<<");
//            return ResponseEntity.ok(sharedPostAssembler.toModel(sharedPost));
//        } catch (PostNotFoundException exception) {
//            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Problem.create().withTitle(" Post Not Found").withDetail(exception.getMessage()));
//        } catch (UserNotFoundException exception) {
//            logger.info(">>>>Error Occurred: " + exception.getMessage() + " @ " +
//                    getTimestamp() + "<<<<");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
//        } catch (ContentIsPrivateException exception) {
//            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Problem.create().withTitle("Action Not Allowed").withDetail(exception.getMessage()));
//        }
//
//    }

//    @DeleteMapping("/sharedPosts/{postId}")
//    @PreAuthorize("#username == authentication.principal.username")
//    public ResponseEntity<?> deleteSharedPost(@PathVariable String username, @PathVariable Long postId) {
//        try {
//            logger.info(">>>>Deleting Shared Post... @ " + getTimestamp() + "<<<<");
//            sharedPostService.deleteSharedPost(username, postId);
//            logger.info(">>>>Shared Post Deleted. @ " + getTimestamp() + "<<<<");
//            return ResponseEntity.noContent().build();
//        } catch (PostNotFoundException exception) {
//            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
//            exception = new PostNotFoundException(postId);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Problem.create().withTitle("Post Not Found").withDetail(exception.getMessage()));
//        } catch (UserNotFoundException exception) {
//            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
//            exception = new UserNotFoundException(username);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
//        } catch (ContentIsPrivateException exception) {
//            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Problem.create().withTitle("Action Not Allowed").withDetail(exception.getMessage()));
//        }
//    }

//    @PutMapping("/sharedPosts/{postId}")
//    @PreAuthorize("#username == authentication.principal.username")
//    public ResponseEntity<?> updateSharedPost(@PathVariable String username, @PathVariable Long postId,
//                                              @Valid @RequestBody Map<String, String> bodyMap) {
//        try {
//            logger.info(">>>>Editing Shared Post... @ " + getTimestamp() + "<<<<");
//            SharedPost updatedSharedPost = sharedPostService.updateSharedPost(postId,
//                    Privacy.valueOf(bodyMap.get("privacy")));
//            EntityModel<SharedPost> sharedPostModel = sharedPostAssembler.toModel(updatedSharedPost);
//            logger.info(">>>>Shared Post Edited.  @ " + getTimestamp() + "<<<<");
//            return ResponseEntity.ok(sharedPostModel);
//        } catch (PostNotFoundException exception) {
//            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
//            exception = new PostNotFoundException(postId);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Problem.create().withTitle("Post Not Found").withDetail(exception.getMessage()));
//        } catch (ContentIsPrivateException exception) {
//            logger.info(">>>>Error Occurred:  " + exception.getMessage() + " @ " + getTimestamp() + "<<<<");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Problem.create().withTitle("Action Not Allowed").withDetail(exception.getMessage()));
//        }
//    }
}
