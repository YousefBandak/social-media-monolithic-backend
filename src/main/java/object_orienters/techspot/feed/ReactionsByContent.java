package object_orienters.techspot.feed;

import object_orienters.techspot.comment.Comment;
import object_orienters.techspot.comment.CommentRepository;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.reaction.Reaction;
import object_orienters.techspot.reaction.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReactionsByContent extends Strategy<Reaction, Long> {
    @Autowired
    public ReactionsByContent(ReactableContentRepository reactableContentRepository, ReactionRepository reactionRepository) {
        this.reactableContentRepository = reactableContentRepository;
        this.reactionRepository = reactionRepository;
    }

    public Page<Reaction> operate(Long contentID, int pageNumber, int pageSize) {
        return reactionRepository.findByContent(reactableContentRepository.findByContentID(contentID).orElseThrow(() -> new ContentNotFoundException(contentID)), PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending()));
    }

}