package object_orienters.techspot.comment;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import object_orienters.techspot.DataTypeUtils;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImpleCommentService implements CommentService {
    private final CommentRepository commentRepository;
    private final ReactableContentRepository contentRepository;
    private final ProfileRepository profileRepository;
    private final DataTypeRepository dataTypeRepository;

    public ImpleCommentService(CommentRepository commentRepository, ReactableContentRepository contentRepository,
            ProfileRepository profileRepository, DataTypeRepository dataTypeRepository) {
        this.commentRepository = commentRepository;
        this.contentRepository = contentRepository;
        this.profileRepository = profileRepository;
        this.dataTypeRepository = dataTypeRepository;
    }

    // @Override
    // public Comment createComment(Comment newComment) {
    // if (newComment == null) {
    // throw new IllegalArgumentException("Comment object cannot be null.");
    // } else {
    // newComment.setCommentedOn(contentRepository.findById(newComment.getContentID())
    // .orElseThrow(() -> new ContentNotFoundException(newComment.getContentID())));
    // return commentRepository.save(newComment);
    // }
    // }

    @Override // FIXME: save by order of the content
    public Comment addComment(Long contentId, String username, MultipartFile file, String text) throws ContentNotFoundException, IOException {

        // if (comment == null || comment.isBlank() || comment.isEmpty()) {
        // throw new IllegalArgumentException("Comment object cannot be null.");
        // } else {
        ReactableContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        DataType comment = new DataType();
        if (file != null && !file.isEmpty()) {
            comment.setData(DataTypeUtils.compress(file.getBytes()));
            comment.setType(file.getContentType());
        }
        comment.setType(comment.getType() != null ? comment.getType() : "text/plain");
        Comment newComment = new Comment(comment, user, content);
        newComment.setTextData(text == null ? "" : text);
        content.setNumOfComments(content.getNumOfComments() + 1);
        content.getComments().add(newComment);
        dataTypeRepository.save(comment);
        commentRepository.save(newComment);
        contentRepository.save(content);
        return newComment;
        // }
    }

    @Override
    public Comment getComment(Long commentId) throws ContentNotFoundException {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ContentNotFoundException(commentId));
    }

    // @Override
    // public List<Comment> getCommentsOfPost(Long postId) throws
    // PostNotFoundException {
    // Post post = postRepository.findById(postId).orElseThrow(() -> new
    // PostNotFoundException(postId));
    // return post.getComments();
    // }
    //
    // @Override
    // public List<Comment> getCommentsOfComment(Long commentId) throws
    // CommentNotFoundException {
    // Comment comment = commentRepository.findById(commentId)
    // .orElseThrow(() -> new CommentNotFoundException(commentId));
    // return comment.getComments();
    // }

    @Override
    public List<Comment> getComments(Long contentId) throws ContentNotFoundException {
        ReactableContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        return content.getComments();
    }

    @Override
    public void deleteComment(Long contentId, Long commentId) throws ContentNotFoundException {
        ReactableContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        content.getComments().removeIf(c -> c.getContentID().equals(commentId));
        content.setNumOfComments(content.getNumOfComments() - 1);
        contentRepository.save(content);

    }

    @Override
    public Comment updateComment(Long contentID, Long commentID, String newComment)
            throws ContentNotFoundException, CommentNotFoundException {
        // Content content = contentRepository.findById(contentID)
        // .orElseThrow(() -> new ContentNotFoundException(contentID));
        // Comment comment = commentRepository.findById(commentID)
        // .orElseThrow(() -> new CommentNotFoundException(commentID));
        // comment.setComment(newComment);
        // return commentRepository.save(comment);
        return null;
    }

    public boolean isCommentAuthor(String username, Long commentID) {
        Optional<Comment> commentOptional = commentRepository.findById(commentID);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            return comment.getContentAuthor().getUsername().equals(username);
        }
        return false; // Return false if the comment is not found
    }

    // @Override
    // public void deletePostComment(Long postId, Long commentId)
    // throws PostNotFoundException, CommentNotFoundException {
    // Post post = postRepository.findById(postId)
    // .orElseThrow(() -> new PostNotFoundException(postId));
    // Comment comment = post.getComments().stream()
    //
    // .filter(c -> c.getContentId().equals(commentId))
    // .findFirst()
    // .orElseThrow(() -> new CommentNotFoundException(commentId));
    // post.getComments().remove(comment);
    // postRepository.save(post);
    // }

}
