package object_orienters.techspot.reaction;

import jakarta.transaction.Transactional;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final ReactableContentRepository contentRepository;
    private final ProfileRepository profileRepository;
    Logger logger = LoggerFactory.getLogger(ReactionController.class);

    public ReactionService(ReactionRepository reactionRepository, ReactableContentRepository contentRepository,
            ProfileRepository profileRepository) {

        this.reactionRepository = reactionRepository;
        this.contentRepository = contentRepository;
        this.profileRepository = profileRepository;
    }

    
    public Reaction getReaction(Long reactionId) throws ReactionNotFoundException {

        return reactionRepository.findByReactionID(reactionId).orElseThrow(() -> new ReactionNotFoundException(reactionId));
    }

    
    public List<Reaction> getReactions(Long contentId) {
        ReactableContent content = contentRepository.findByContentID(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        return content.getReactions();
    }

    
    public Reaction updateReaction(Long reactionId, String reactionType) {
        Reaction existingReaction = reactionRepository.findByReactionID(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));
        Reaction.ReactionType reactionTypee = Reaction.ReactionType.valueOf(reactionType);
        existingReaction.setType(reactionTypee);
        reactionRepository.save(existingReaction);
        return existingReaction;
    }

    
    @Transactional 
    public void deleteReaction(Long reactionId) {
        ReactableContent content = reactionRepository.findByReactionID(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId)).getContent();
        content.getReactions().remove(reactionRepository.findByReactionID(reactionId).get());
        content.setNumOfReactions(content.getNumOfReactions() - 1);
        contentRepository.save(content);
        reactionRepository.deleteByReactionID(reactionId);
    }

    
    public Reaction createReaction(String reactorID, String reactionType, Long contentId) {
        Reaction.ReactionType reactionTypee = Reaction.ReactionType.valueOf(reactionType);
        Profile reactor = profileRepository.findById(reactorID)
                .orElseThrow(() -> new UserNotFoundException(reactorID));
        ReactableContent content = contentRepository.findByContentID(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        content.setNumOfReactions(content.getNumOfReactions() + 1);
        Reaction createdReaction = new Reaction(reactor, reactionTypee, content);
        return reactionRepository.save(createdReaction);

    }

    public boolean isReactor(String username, Long reactionID) {
        Optional<Reaction> reactionOptional = reactionRepository.findByReactionID(reactionID);
        if (reactionOptional.isPresent()) {
            Reaction reaction = reactionOptional.get();
            logger.info(">>>>Checking if the user is the reactor... @ "
                    + getReaction(reactionID).getReactor().getUsername() + "<<<<");
            boolean x = reaction.getReactor().getUsername().equals(username);
            if (x) {
                return true;
            }
            return false;
        }
        return false;
    }

}
