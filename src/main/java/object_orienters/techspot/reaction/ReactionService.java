package object_orienters.techspot.reaction;

import jakarta.transaction.Transactional;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.exceptions.ContentNotFoundException;
import object_orienters.techspot.exceptions.ReactionNotFoundException;
import object_orienters.techspot.exceptions.UserNotFoundException;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final ReactableContentRepository contentRepository;
    private final ProfileRepository profileRepository;
    Logger logger = LoggerFactory.getLogger(ReactionController.class);

    public ReactionService(ReactionRepository reactionRepository,
                           ReactableContentRepository contentRepository,
                           ProfileRepository profileRepository) {

        this.reactionRepository = reactionRepository;
        this.contentRepository = contentRepository;
        this.profileRepository = profileRepository;
    }

    public Reaction getReaction(String reactionId) throws ReactionNotFoundException {

        return reactionRepository.findByReactionID(reactionId).orElseThrow(() -> new ReactionNotFoundException(reactionId));
    }

    public Page<Reaction> getReactions(Long contentId, int pageNumber, int pageSize) {
//        ReactableContent content = contentRepository.findByContentID(contentId)
//                .orElseThrow(() -> new ContentNotFoundException(contentId));
//        return content.getReactions();
        return reactionRepository.findByContent(contentRepository.findByContentID(contentId).orElseThrow(() -> new ContentNotFoundException(contentId)), PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending()));

    }

    public Page<Reaction> getReactionsByType(Long contentId, String reactionType, int pageNumber, int pageSize) {
        return reactionRepository.findByContentAndType(contentRepository.findByContentID(contentId).orElseThrow(() -> new ContentNotFoundException(contentId)), Reaction.ReactionType.valueOf(reactionType), PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending()));
    }

    @Transactional
    public void deleteReaction(String reactionId) {
        Reaction reaction = reactionRepository.findByReactionID(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));
        ReactableContent content = reaction.getContent();
        content.getReactions().remove(reaction);
        content.setNumOfReactions(content.getNumOfReactions() - 1);
        contentRepository.save(content);
        reactionRepository.deleteByReactionID(reactionId);
    }

    public Reaction createReaction(String reactorID, String reactionType, Long contentId) {

        Reaction.ReactionType reactionTypeEnum = Reaction.ReactionType.valueOf(reactionType);

        Profile reactor = profileRepository.findById(reactorID)
                .orElseThrow(() -> new UserNotFoundException(reactorID));

        ReactableContent content = contentRepository.findByContentID(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        AtomicReference<Reaction> createdReaction = new AtomicReference<>();
        content.getReactions().stream()
                .filter(reaction -> reaction.getReactor().getUsername().equals(reactor.getUsername()))
                .findFirst().ifPresentOrElse(reaction -> {
                    reaction.setType(reactionTypeEnum);
                    reaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
                    reaction.setReactionID(reactor.getUsername() + content.getContentID());
                    createdReaction.set(reaction);

                }, () -> {
                    Reaction newReaction = new Reaction(reactor, reactionTypeEnum, content);
                    newReaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
                    newReaction.setReactionID(reactor.getUsername() + content.getContentID());
                    content.getReactions().add(newReaction);
                    content.setNumOfReactions(content.getNumOfReactions() + 1);
                    contentRepository.save(content);
                    createdReaction.set(newReaction);
                });

        return reactionRepository.save(createdReaction.get());

    }

    public Map<String, ?> isReactor(String username, String reactionID) {
        Optional<Reaction> reactionOptional = reactionRepository.findByReactionID(reactionID);
        if (reactionOptional.isPresent()) {
            Reaction reaction = reactionOptional.get();
            logger.info(">>>>Checking if the user is the reactor... @ "
                    + getReaction(reactionID).getReactor().getUsername() + "<<<<");
            boolean x = reaction.getReactor().getUsername().equals(username);
            Map<String, ?> map = Map.of("isReactor", x, "reactionType", reaction.getType());
            return map;
        }
        return Map.of("isReactor", false);
    }

}
