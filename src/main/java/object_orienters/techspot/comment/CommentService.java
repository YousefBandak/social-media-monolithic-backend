package object_orienters.techspot.comment;

import java.util.List;

import object_orienters.techspot.post.PostNotFoundException;

public interface CommentService {
    public List<Comment> getCommentsOfPost(Long postId) throws PostNotFoundException;

    public Comment addCommentToPost(Long postId, Comment newComment) throws PostNotFoundException;

    public void deletePostComment(Long postId, Long commentId) throws PostNotFoundException, CommentNotFoundException;
}
