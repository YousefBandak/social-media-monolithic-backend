package object_orienters.techspot.comment;

import object_orienters.techspot.FileStorageService;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.postTypes.DataTypeRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.reaction.Reaction;
import object_orienters.techspot.reaction.ReactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReactableContentRepository contentRepository;
    private final ProfileRepository profileRepository;
    private final DataTypeRepository dataTypeRepository;
    private final ReactionRepository reactionRepository;
    private final FileStorageService fileStorageService;

    public CommentService(CommentRepository commentRepository, ReactableContentRepository contentRepository,
            ProfileRepository profileRepository, DataTypeRepository dataTypeRepository,
            ReactionRepository reactionRepository, FileStorageService fileStorageService) {
        this.commentRepository = commentRepository;
        this.contentRepository = contentRepository;
        this.profileRepository = profileRepository;
        this.dataTypeRepository = dataTypeRepository;
        this.reactionRepository = reactionRepository;
        this.fileStorageService = fileStorageService;
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
        } else if (content.getPrivacy().equals(Privacy.FRIENDS)
                && !content.getContentAuthor().getFollowers().contains(prof)) {
            throw new ContentNotFoundException(contentId);
        }
        List<DataType> allMedia = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            handleAddMediaData(files, allMedia);
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

    public List<Comment> getComments(Long contentId) throws ContentNotFoundException {
        ReactableContent content = contentRepository.findByContentID(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        return content.getComments();
    }

    @Transactional
    public void deleteComment(Long contentId, Long commentId) throws ContentNotFoundException {
        ReactableContent content = contentRepository.findByContentID(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        Comment com = commentRepository.findByContentID(commentId).get();
        List<Reaction> reactions = com.getReactions();
        List<Comment> comments = com.getComments();
        handleDeleteMediaData(com);
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
            handleDeleteMediaData(comment);
            handleAddMediaData(files, allMedia);
        }
        comment.setMediaData(allMedia);
        comment.setTextData(text != null ? text : "");
        allMedia.forEach(media -> {
            media.setContent(comment);
        });
        dataTypeRepository.saveAll(allMedia);
        return commentRepository.save(comment);

    }

    public boolean isCommentAuthor(String username, Long commentID) {
        Optional<Comment> commentOptional = commentRepository.findByContentID(commentID);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            return comment.getContentAuthor().getUsername().equals(username);
        }
        return false;
    }

    private void handleAddMediaData(List<MultipartFile> files, List<DataType> allMedia) {
        files.stream().forEach((file) -> {
            DataType media = new DataType();
            String fileName = fileStorageService.storeFile(file);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/media_uploads/")
                    .path(fileName)
                    .toUriString();
            media.setType(file.getContentType());
            media.setFileName(fileName);
            media.setFileUrl(fileDownloadUri);
            allMedia.add(media);
        });
    }

    private void handleDeleteMediaData(Comment comment) {
        comment.getMediaData().stream().forEach(media -> {
            fileStorageService.deleteFile(media.getFileName());
            dataTypeRepository.delete(media);
        });
    }
}
