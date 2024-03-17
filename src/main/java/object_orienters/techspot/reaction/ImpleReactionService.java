package object_orienters.techspot.reaction;


import object_orienters.techspot.content.Content;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ContentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpleReactionService implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final ContentRepository contentRepository;

    public ImpleReactionService(ReactionRepository reactionRepository, ContentRepository contentRepository) {

        this.reactionRepository = reactionRepository;
        this.contentRepository = contentRepository;
    }

    @Override
    public Reaction createReaction(Reaction reaction) {
        if (reaction == null) {
            throw new IllegalArgumentException("Reaction object cannot be null.");
        } else
            return reactionRepository.save(reaction);

    }

    @Override
    public Reaction getReaction(String reactionId) {
        return reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));
    }

    @Override
    public String deleteReaction(String reactionId) {
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));
        reactionRepository.deleteById(reactionId);
        return "Reaction with ID: " + reaction.getReactionId() + " deleted successfully";
    }

    @Override
    public List<Reaction> getReactions(Long contentId) {
        return reactionRepository.findByContent(contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId)));
    }
}
