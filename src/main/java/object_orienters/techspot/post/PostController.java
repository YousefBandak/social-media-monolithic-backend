package object_orienters.techspot.post;

import object_orienters.techspot.profile.UserNotFoundException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class PostController {
    // private static final Logger log =
    // LoggerFactory.getLogger(LoadDatabase.class);
    private final PostModelAssembler assembler;
    private final PostService postService;

    PostController(PostModelAssembler assembler,
            PostService postService) {
        this.assembler = assembler;
        this.postService = postService;
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<?> getTimelinePosts(@PathVariable String username) {
        try {
            return ResponseEntity.ok(assembler.toCollectionModel(postService.getTimelinePosts(username)));
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    @PostMapping("/{username}/posts")
    public ResponseEntity<?> addTimelinePosts(@PathVariable String username, @RequestBody Post post) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(assembler.toModel(postService.addTimelinePosts(username, post)));
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    @PutMapping("/{username}/posts/{postId}")
    public ResponseEntity<?> editTimelinePost(@PathVariable String username, @PathVariable long postId,
            @RequestBody Post newPost) {
        try {
            return ResponseEntity.ok(assembler.toModel(postService.editTimelinePost(username, postId, newPost)));
        } catch (UserNotFoundException | PostNotFoundException | PostUnrelatedToUserException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(exception.getMessage()));
        }

    }

    @DeleteMapping("/{username}/posts/{postId}")
    public ResponseEntity<?> deleteTimelinePost(@PathVariable String username, @PathVariable long postId) {
        try {
            postService.deleteTimelinePost(username, postId);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException | PostNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(exception.getMessage()));
        }

    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPost(@PathVariable long postId) {
        try {
            return ResponseEntity.ok(assembler.toModel(postService.getPost(postId)));
        } catch (PostNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Post Not Found").withDetail(exception.getMessage()));
        }

    }

    // //TODO: make sure the post has the same author as the user, otherwise anyone
    // can edit any post and make themselves the author
    // //TODO: Specify if post is shared or authored

}
