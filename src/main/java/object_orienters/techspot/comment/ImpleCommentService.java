package object_orienters.techspot.comment;

import java.util.List;

import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.post.PostNotFoundException;
import object_orienters.techspot.post.Post;

public class ImpleCommentService implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public ImpleCommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Comment> getCommentsOfPost(Long postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return post.getComments();
    }

    @Override
    public Comment addCommentToPost(Long postId, Comment newComment) throws PostNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        newComment.setCommentedOn(post);
        Comment savedComment = commentRepository.save(newComment);
        post.getComments().add(newComment);
        postRepository.save(post);
        return savedComment;
    }

    @Override
    public void deletePostComment(Long postId, String commentId)
            throws PostNotFoundException, CommentNotFoundException {
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
