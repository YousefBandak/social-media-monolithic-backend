package object_orienters.techspot.controller;

import object_orienters.techspot.CommentModelAssembler;
import object_orienters.techspot.PostRepository;
import object_orienters.techspot.exception.CommentNotFoundException;
import object_orienters.techspot.exception.PostNotFoundException;
import object_orienters.techspot.model.Comment;
import object_orienters.techspot.model.Post;
import object_orienters.techspot.repository.CommentRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

public class CommentController {

    //private  CommentService commentService;
    private CommentModelAssembler commentModelAssembler;
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    CommentController(CommentRepository commentRepository, PostRepository postRepository , CommentModelAssembler commentModelAssembler){
        this.commentModelAssembler = commentModelAssembler;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;

    }

    @GetMapping("/posts/{postId}/comments")
    public CollectionModel<EntityModel<Comment>> getCommentsOfPost(@PathVariable long postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return  commentModelAssembler.toCollectionModel(post.getComments());
    }

    @PostMapping("/posts/{postId}/comments")
    public EntityModel<Comment> addCommentToPost(@PathVariable long postId, @RequestBody Comment newComment) throws PostNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        newComment.setCommentedOn(post);
        Comment savedComment = commentRepository.save(newComment);
        post.getComments().add(newComment);
        postRepository.save(post);
        return commentModelAssembler.toModel(savedComment);
    }


    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public void deleteComment(@PathVariable long postId, @PathVariable String commentId) throws PostNotFoundException, CommentNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        Comment comment = post.getComments().stream()
                .filter(c -> c.getCommentId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        post.getComments().remove(comment);
        postRepository.save(post);
    }

}
