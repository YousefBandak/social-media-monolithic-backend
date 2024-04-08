package object_orienters.techspot.comment;

import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.post.PostNotFoundException;
import object_orienters.techspot.profile.Profile;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface CommentService {

        public Comment getComment(Long commentId) throws CommentNotFoundException;

        public List<Comment> getComments(Long contentId) throws ContentNotFoundException;

        public Comment addComment(Long contentId, String username, MultipartFile file, String text)
                        throws PostNotFoundException, ContentNotFoundException, IOException;

        public void deleteComment(Long contentId, Long commentId)
                        throws PostNotFoundException, ContentNotFoundException, CommentNotFoundException;

        public Comment updateComment(Long contentID, Long commentID, MultipartFile file, String text)
                        throws ContentNotFoundException, CommentNotFoundException, IOException;

}
