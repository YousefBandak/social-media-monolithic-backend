package object_orienters.techspot.service.interfaces;

import java.util.List;

import object_orienters.techspot.exception.CommentNotFoundException;
import object_orienters.techspot.exception.PostNotFoundException;
import object_orienters.techspot.model.Comment;

public interface CommentService {
    public List<Comment> getCommentsOfPost(Long postId) throws PostNotFoundException;

    public Comment addCommentToPost(Long postId, Comment newComment) throws PostNotFoundException;

    public void deletePostComment(Long postId, String commentId) throws PostNotFoundException, CommentNotFoundException;
}
