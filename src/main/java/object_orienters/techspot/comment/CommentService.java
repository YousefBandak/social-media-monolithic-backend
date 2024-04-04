package object_orienters.techspot.comment;

import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.post.PostNotFoundException;
import object_orienters.techspot.reaction.Reaction;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface CommentService {
    // public Comment createComment(Comment newComment);
    public Comment getComment(Long commentId) throws CommentNotFoundException;

    public List<Comment> getComments(Long contentId) throws ContentNotFoundException;

    public Comment addComment(Long contentId, String username, MultipartFile file, String text) throws PostNotFoundException,ContentNotFoundException, IOException;

    // public void deletePostComment(Long postId, Long commentId) throws
    // PostNotFoundException, CommentNotFoundException;
    public void deleteComment(Long contentId, Long commentId)
            throws PostNotFoundException, ContentNotFoundException, CommentNotFoundException;

    public Comment updateComment(Long contentID, Long commentID, String newComment)
            throws ContentNotFoundException, CommentNotFoundException;

    // public List<Comment> getCommentsOfPost(Long postId) throws
    // PostNotFoundException;
    // public List<Comment> getCommentsOfComment(Long comment) throws
    // PostNotFoundException, CommentNotFoundException;

}
