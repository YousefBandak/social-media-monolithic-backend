package object_orienters.techspot.post;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.UserNotFoundException;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles/{username}/posts")
public class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostModelAssembler assembler;
    private final SharedPostModelAssembler sharedPostAssembler;
    private final PostService postService;
    private final SharedPostService sharedPostService;

    PostController(PostModelAssembler assembler, PostService postService, SharedPostModelAssembler sharedPostAssembler,
            SharedPostService sharedPostService) {
        this.assembler = assembler;
        this.postService = postService;
        this.sharedPostAssembler = sharedPostAssembler;
        this.sharedPostService = sharedPostService;
    }

    @GetMapping("")
    public ResponseEntity<?> getTimelinePosts(@PathVariable String username) {
        try {
            return ResponseEntity.ok(assembler.toCollectionModel(postService.getTimelinePosts(username)));
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addTimelinePosts(@PathVariable String username, @RequestBody Post post,
            @RequestParam(required = false) boolean isShared) {
        if (isShared) {
            try {
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        sharedPostAssembler.toModel(postService.addSharedPost(username, post, post.getPrivacy())));
            } catch (UserNotFoundException exception) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
            }
        } else {
            try {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(assembler.toModel(postService.addTimelinePosts(username, post)));
            } catch (UserNotFoundException exception) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
            }
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> editTimelinePost(@PathVariable String username, @PathVariable long postId,
            @RequestBody Post newPost) {
        try {
            return ResponseEntity.ok(assembler.toModel(postService.editTimelinePost(username, postId, newPost)));
        } catch (UserNotFoundException | PostNotFoundException | PostUnrelatedToUserException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(exception.getMessage()));
        }

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteTimelinePost(@PathVariable String username, @PathVariable long postId) {
        try {
            postService.deleteTimelinePost(username, postId);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException | PostNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(exception.getMessage()));
        }

    }

    // Todo: the path need to be changed
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable long postId, @PathVariable String username) {
        try {
            return ResponseEntity.ok(assembler.toModel(postService.getPost(postId)));
        } catch (PostNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Post Not Found").withDetail(exception.getMessage()));
        }

    }

    // //TODO: Specify if post is shared or authored

    @PostMapping("/{postId}/share")
    public ResponseEntity<?> sharePostOfUser(@PathVariable String username, @RequestBody Map<String,String> newSharedPost) {
        try {
            sharedPostService.addSharedPost(username, post, privacy);
        } catch (UserNotFoundException | PostNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
