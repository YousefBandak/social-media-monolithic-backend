package object_orienters.techspot.controller;

import object_orienters.techspot.controller.assemblers.CommentModelAssembler;
import object_orienters.techspot.exception.CommentNotFoundException;
import object_orienters.techspot.exception.PostNotFoundException;
import object_orienters.techspot.model.Comment;
import object_orienters.techspot.service.interfaces.CommentService;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

public class CommentController {

    private CommentModelAssembler commentModelAssembler;
    private CommentService commentService;

    CommentController(CommentModelAssembler commentModelAssembler, CommentService commentService) {
        this.commentModelAssembler = commentModelAssembler;
        this.commentService = commentService;
    }

    @GetMapping("/posts/{postId}/comments")
    public CollectionModel<EntityModel<Comment>> getCommentsOfPost(@PathVariable long postId)
            throws PostNotFoundException {
        return commentModelAssembler.toCollectionModel(commentService.getCommentsOfPost(postId));
    }

    @PostMapping("/posts/{postId}/comments")
    public EntityModel<Comment> addCommentToPost(@PathVariable long postId, @RequestBody Comment newComment)
            throws PostNotFoundException {
        return commentModelAssembler.toModel(commentService.addCommentToPost(postId, newComment));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public void deleteComment(@PathVariable long postId, @PathVariable String commentId)
            throws PostNotFoundException, CommentNotFoundException {
        commentService.deletePostComment(postId, commentId);
    }

}
