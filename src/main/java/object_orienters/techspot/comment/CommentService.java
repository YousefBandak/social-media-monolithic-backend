package object_orienters.techspot.comment;

import jakarta.transaction.Transactional;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.exceptions.CommentNotFoundException;
import object_orienters.techspot.exceptions.ContentNotFoundException;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.reaction.Reaction;
import object_orienters.techspot.reaction.ReactionRepository;
import object_orienters.techspot.utilities.FileStorageService;
import object_orienters.techspot.utilities.MediaDataUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReactableContentRepository contentRepository;
    private final ProfileRepository profileRepository;
    private final DataTypeRepository dataTypeRepository;
    private final ReactionRepository reactionRepository;
    private final FileStorageService fileStorageService;
    private final ReactableContentRepository reactableContentRepository;
    private final MediaDataUtilities mediaDataUtilities;

    public CommentService(CommentRepository commentRepository,
                          ReactableContentRepository contentRepository,
                          ProfileRepository profileRepository,
                          DataTypeRepository dataTypeRepository,
                          ReactionRepository reactionRepository,
                          FileStorageService fileStorageService,
                          ReactableContentRepository reactableContentRepository,
                          MediaDataUtilities mediaDataUtilities) {
        this.commentRepository = commentRepository;
        this.contentRepository = contentRepository;
        this.profileRepository = profileRepository;
        this.dataTypeRepository = dataTypeRepository;
        this.reactionRepository = reactionRepository;
        this.fileStorageService = fileStorageService;
        this.reactableContentRepository = reactableContentRepository;
        this.mediaDataUtilities = mediaDataUtilities;
    }

    @Transactional
    public Comment addComment(Long contentId, String username, List<MultipartFile> files, String text)
            throws ContentNotFoundException, IOException {
        ReactableContent content = contentRepository.findByContentID(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        Profile prof = profileRepository.findById(username).get();
        if (content.getPrivacy().equals(Privacy.PRIVATE)
                && !content.getContentAuthor().getUsername().equals(username)) {
            throw new ContentNotFoundException(contentId);
        } else if (content.getPrivacy().equals(Privacy.FRIENDS) && !content.getContentAuthor().getFollowers().contains(prof)) {

            throw new ContentNotFoundException(contentId);
        }
        List<DataType> allMedia = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            mediaDataUtilities.handleAddMediaData(files, allMedia);
        }
        Comment newComment = new Comment();
        newComment.setMediaData(allMedia);
        newComment.setContentAuthor(prof);
        newComment.setCommentedOn(content);
        newComment.setTextData(text != null ? text : "");
        content.setNumOfComments(content.getNumOfComments() + 1);
        content.getComments().add(newComment);
        allMedia.forEach(media -> {
            media.setContent(newComment);
        });
        dataTypeRepository.saveAll(allMedia);
        commentRepository.save(newComment);
        contentRepository.save(content);
        return newComment;
    }


    public Comment getComment(Long commentId) throws ContentNotFoundException {
        return commentRepository.findByContentID(commentId)
                .orElseThrow(() -> new ContentNotFoundException(commentId));
    }


    public Page<Comment> getComments(Long contentId, int pageNumber, int pageSize) throws ContentNotFoundException {
        return commentRepository.findByCommentedOn(reactableContentRepository.findByContentID(contentId).orElseThrow(() -> new ContentNotFoundException(contentId)), PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending()));

    }

    @Transactional
    public void deleteComment(Long contentId, Long commentId) throws ContentNotFoundException {
        ReactableContent content = contentRepository.findByContentID(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        Comment com = commentRepository.findByContentID(commentId).get();
        List<Reaction> reactions = com.getReactions();
        List<Comment> comments = com.getComments();
        mediaDataUtilities.handleDeleteMediaData(com);
        com.setMediaData(null);
        com.setReactions(new ArrayList<>());
        com.setComments(new ArrayList<>());
        com.setContentAuthor(null);
        com.setCommentedOn(null);
        content.getComments().removeIf(c -> c.getContentID().equals(commentId));
        content.setNumOfComments(content.getNumOfComments() - 1);
        reactions.stream().forEach(reaction -> {
            Profile prof = reaction.getReactor();
            reaction.setContent(null);
            reaction.setReactor(null);
            reactionRepository.delete(reaction);
            profileRepository.save(prof);
        });
        comments.stream().forEach(comment -> {
            Profile prof = comment.getContentAuthor();
            comment.setCommentedOn(null);
            comment.setContentAuthor(null);
            commentRepository.delete(comment);
            profileRepository.save(prof);
        });
        commentRepository.delete(commentRepository.findByContentID(commentId).get());
        contentRepository.save(content);

    }


    @Transactional
    public Comment updateComment(Long contentID, Long commentID, List<MultipartFile> files, String text)
            throws ContentNotFoundException, CommentNotFoundException, IOException {
        contentRepository.findByContentID(contentID).orElseThrow(() -> new ContentNotFoundException(contentID));
        Comment comment = commentRepository.findByContentID(commentID)
                .orElseThrow(() -> new CommentNotFoundException(commentID));
        List<DataType> allMedia = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            mediaDataUtilities.handleDeleteMediaData(comment);
            mediaDataUtilities.handleAddMediaData(files, allMedia);
        }
        comment.setMediaData(allMedia);
        comment.setTextData(text != null ? text : "");
        allMedia.forEach(media -> {
            media.setContent(comment);
        });
        dataTypeRepository.saveAll(allMedia);
        return commentRepository.save(comment);

    }
}
