package object_orienters.techspot.comment;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ContentRepository;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostController;
import object_orienters.techspot.post.PostNotFoundException;

import object_orienters.techspot.reaction.Reaction;
import object_orienters.techspot.reaction.ReactionController;
import org.slf4j.Logger;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/posts/{contentID}")
public class CommentController {

    private final CommentModelAssembler assembler;
    private final ImpleCommentService commentService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(CommentController.class);

    CommentController(CommentModelAssembler commentModelAssembler, ImpleCommentService commentService) {
        this.assembler = commentModelAssembler;
        this.commentService = commentService;
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getComments(@PathVariable long contentID) {

        try {
            List<Comment> commentList = commentService.getComments(contentID);
            CollectionModel<EntityModel<Comment>> commentModel = CollectionModel.of(commentList.stream().map(assembler::toModel).collect(Collectors.toList()), linkTo(methodOn(CommentController.class).getComments(contentID)).withSelfRel(), linkTo(methodOn(PostController.class).getPost(contentID)).withRel("post"));
            return ResponseEntity.ok(commentModel);
        } catch (ContentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable Long commentId) {
        try {
            Comment comment = commentService.getComment(commentId);
            EntityModel<Comment> commentModel = assembler.toModel(comment);
            return ResponseEntity.ok(commentModel);
        } catch ( ContentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable long contentID, @PathVariable Long commentId, @RequestBody Comment newComment) {
        try {
            Comment updatedComment = commentService.updateComment(contentID, commentId, newComment);
            EntityModel<Comment> commentModel = assembler.toModel(updatedComment);
            return ResponseEntity.ok(commentModel);
        } catch (ContentNotFoundException | CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }
    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@PathVariable long contentID, @RequestBody Comment newComment, @RequestParam String username) {
        try {
            Comment createdComment = commentService.addComment(contentID, newComment, username);
            logger.info("Comment added to the post: " + createdComment);
            EntityModel<Comment> commentModel = assembler.toModel(createdComment);
            return ResponseEntity.status(HttpStatus.CREATED).body(commentModel);
        } catch ( IllegalArgumentException | ContentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @DeleteMapping("/comments/{commentId}")

    public ResponseEntity<?> deleteComment(@PathVariable long contentID, @PathVariable Long commentId)  {
        try {
            commentService.deleteComment(contentID, commentId);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (ContentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }



}
