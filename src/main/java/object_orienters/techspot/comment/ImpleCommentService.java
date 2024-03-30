package object_orienters.techspot.comment;

import java.util.List;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ImpleCommentService implements CommentService {
    private final CommentRepository commentRepository;
    private final ReactableContentRepository contentRepository;
    private final ProfileRepository profileRepository;

    public ImpleCommentService(CommentRepository commentRepository, ReactableContentRepository contentRepository,
            ProfileRepository profileRepository) {
        this.commentRepository = commentRepository;
        this.contentRepository = contentRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public Comment createComment(Comment newComment) {
        if (newComment == null) {
            throw new IllegalArgumentException("Comment object cannot be null.");
        } else {
            newComment.setCommentedOn(contentRepository.findById(newComment.getContentID())
                    .orElseThrow(() -> new ContentNotFoundException(newComment.getContentID())));
            return commentRepository.save(newComment);
        }
    }

    @Override // FIXME: save by order of the content
    public Comment addComment(Long contentId, String comment, String username) throws ContentNotFoundException {

        if (comment == null || comment.isBlank() || comment.isEmpty()) {
            throw new IllegalArgumentException("Comment object cannot be null.");
        } else {
            ReactableContent content = contentRepository.findById(contentId)
                    .orElseThrow(() -> new ContentNotFoundException(contentId));
            Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
            Comment newComment = new Comment(comment, user, content);
            newComment.setCommentedOn(content);
            newComment.setContentAuthor(user);
            commentRepository.save(newComment);
            content.getComments().add(newComment);
            contentRepository.save(content);
            return newComment;
        }
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
        contentRepository.save(content);

    }

    @Override
    public Comment updateComment(Long contentID, Long commentID, String newComment)
            throws ContentNotFoundException, CommentNotFoundException {
        Content content = contentRepository.findById(contentID)
                .orElseThrow(() -> new ContentNotFoundException(contentID));
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new CommentNotFoundException(commentID));
        comment.setComment(newComment);
        return commentRepository.save(comment);

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
