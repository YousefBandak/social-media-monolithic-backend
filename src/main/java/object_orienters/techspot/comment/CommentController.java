package object_orienters.techspot.comment;

import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.post.PostController;

import org.slf4j.Logger;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/profiles/{username}/posts/{contentID}")
public class CommentController {

    private final CommentModelAssembler assembler;
    private final ImpleCommentService commentService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(CommentController.class);

    CommentController(CommentModelAssembler commentModelAssembler, ImpleCommentService commentService) {
        this.assembler = commentModelAssembler;
        this.commentService = commentService;
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getComments(@PathVariable long contentID, @PathVariable String username) {

        try {
            List<Comment> commentList = commentService.getComments(contentID);
            CollectionModel<EntityModel<Comment>> commentModel = CollectionModel.of(commentList.stream().
                    map(assembler::toModel).collect(Collectors.toList()),
                    linkTo(methodOn(CommentController.class).getComments(contentID,username)).withSelfRel(),
                    linkTo(methodOn(PostController.class).getPost(contentID,username)).withRel("post"));
            return ResponseEntity.ok(commentModel);
        } catch (ContentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @GetMapping("/comments/{commentID}")
    public ResponseEntity<?> getComment(@PathVariable Long commentID, @PathVariable Long contentID, @PathVariable String username) {
        try {
            Comment comment = commentService.getComment(commentID);
            EntityModel<Comment> commentModel = assembler.toModel(comment);
            return ResponseEntity.ok(commentModel);
        } catch ( ContentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @PutMapping("/comments/{commentID}")
    public ResponseEntity<?> updateComment(@PathVariable long contentID, @PathVariable Long commentID, @RequestBody Map<String,String> newComment) {
        try {
            Comment updatedComment = commentService.updateComment(contentID, commentID, newComment.get("comment"));
            logger.info("Comment: " + commentID + "updated to the content: " + contentID);
            EntityModel<Comment> commentModel = assembler.toModel(updatedComment);
            return ResponseEntity.ok(commentModel);
        } catch (ContentNotFoundException | CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }
    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@PathVariable long contentID, @RequestBody Map<String,String> newComment) {
        logger.info(newComment.toString());


        try {
            Comment createdComment = commentService.addComment(contentID, newComment.get("comment"), newComment.get("commentor"));
            logger.info("Comment added to the post: " + createdComment);
            EntityModel<Comment> commentModel = assembler.toModel(createdComment);
            return ResponseEntity.status(HttpStatus.CREATED).body(commentModel);
        } catch ( IllegalArgumentException | ContentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @DeleteMapping("/comments/{commentID}")

    public ResponseEntity<?> deleteComment(@PathVariable long contentID, @PathVariable Long commentID)  {
        try {
            commentService.deleteComment(contentID, commentID);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (ContentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }



}
