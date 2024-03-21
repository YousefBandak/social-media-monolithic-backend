package object_orienters.techspot.comment;

import java.util.List;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ContentRepository;
import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.post.PostNotFoundException;
import object_orienters.techspot.post.Post;

import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.reaction.ReactionNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ImpleCommentService implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ContentRepository contentRepository;
    private final ProfileRepository profileRepository;

    public ImpleCommentService(CommentRepository commentRepository, PostRepository postRepository, ContentRepository contentRepository, ProfileRepository profileRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.contentRepository = contentRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public Comment createComment(Comment newComment) {
        if (newComment == null) {
            throw new IllegalArgumentException("Reaction object cannot be null.");
        } else {
            newComment.setCommentedOn(contentRepository.findById(newComment.getContentId()).orElseThrow(() -> new ContentNotFoundException(newComment.getContentId())));
            return commentRepository.save(newComment);
        }
    }

    @Override //FIXME: save by order of the content
    public Comment addComment(Long contentId, Comment newComment, String username) throws ContentNotFoundException {
        if (newComment == null) {
            throw new IllegalArgumentException("Comment object cannot be null.");
        } else {
            Content content = contentRepository.findById(contentId).orElseThrow(() -> new ContentNotFoundException(contentId));
            Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
            newComment.setCommentedOn(content);
            newComment.setCommenter(user);
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

//    @Override
//    public List<Comment> getCommentsOfPost(Long postId) throws PostNotFoundException {
//        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
//        return post.getComments();
//    }
//
//    @Override
//    public List<Comment> getCommentsOfComment(Long commentId) throws CommentNotFoundException {
//        Comment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new CommentNotFoundException(commentId));
//        return comment.getComments();
//    }

    @Override
    public List<Comment> getComments(Long contentId) throws ContentNotFoundException {
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new ContentNotFoundException(contentId));
        return content.getComments();
    }

    @Override
    public void deleteComment(Long contentId, Long commentId) throws ContentNotFoundException {
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new ContentNotFoundException(contentId));
        content.getComments().removeIf(c -> c.getContentId().equals(commentId));
        contentRepository.save(content);

    }

    @Override
    public Comment updateComment(Long contentID, Long commentID, Comment newComment) throws ContentNotFoundException, CommentNotFoundException {
        Content content = contentRepository.findById(contentID).orElseThrow(() -> new ContentNotFoundException(contentID));
        Comment comment = commentRepository.findById(commentID).orElseThrow(() -> new CommentNotFoundException(commentID));
        comment.setComment(newComment.getComment());
        return commentRepository.save(comment);

    }


//    @Override
//    public void deletePostComment(Long postId, Long commentId)
//            throws PostNotFoundException, CommentNotFoundException {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new PostNotFoundException(postId));
//        Comment comment = post.getComments().stream()
//
//                .filter(c -> c.getContentId().equals(commentId))
//                .findFirst()
//                .orElseThrow(() -> new CommentNotFoundException(commentId));
//        post.getComments().remove(comment);
//        postRepository.save(post);
//    }




}
