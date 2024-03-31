package object_orienters.techspot.reaction;


import jakarta.transaction.Transactional;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImpleReactionService implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final ReactableContentRepository contentRepository;
    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;

    public ImpleReactionService(ReactionRepository reactionRepository, ReactableContentRepository contentRepository,
            ProfileRepository profileRepository, PostRepository postRepository) {

        this.reactionRepository = reactionRepository;
        this.contentRepository = contentRepository;
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
    }

    // @Override
    // public Reaction createReaction(Reaction reaction) {
    // if (reaction == null) {
    // throw new IllegalArgumentException("Reaction object cannot be null.");
    // } else {
    // Content content =
    // contentRepository.findById(reaction.getContent().getContentId()).orElseThrow(()
    // -> new ContentNotFoundException(reaction.getContent().getContentId()));
    // reaction.setContent(content);
    // content.getReactions().add(reaction);
    // return reactionRepository.save(reaction);
    // }
    //
    // }

    @Override
    public Reaction getReaction(Long reactionId) throws ReactionNotFoundException {

        return reactionRepository.findById(reactionId).orElseThrow(() -> new ReactionNotFoundException(reactionId));
    }

    @Override
    public List<Reaction> getReactions(Long contentId) {
        ReactableContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        return content.getReactions();
    }

    @Override
    public Reaction updateReaction(Long reactionId, String reactionType) {
        Reaction existingReaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId));
        Reaction.ReactionType reactionTypee = Reaction.ReactionType.valueOf(reactionType);
        existingReaction.setType(reactionTypee);
        return reactionRepository.save(existingReaction);
    }

    @Override
    @Transactional
    public void deleteReaction(Long reactionId) {
        ReactableContent content = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionNotFoundException(reactionId)).getContent();
        content.getReactions().remove(reactionRepository.findById(reactionId).get());
        // reactionRepository.save(reactionRepository.findById(reactionId).get());
        content.setNumOfReactions(content.getNumOfReactions() - 1);
        contentRepository.save(content);
        // content.getReactions().removeIf(r -> r.getReactionID().equals(reactionId));
        // reactionRepository.findById(reactionId).ifPresent(reactionRepository::delete);
        reactionRepository.deleteByReactionID(reactionId);
    }

    @Override
    public Reaction createReaction(String reactorID, String reactionType, Long contentId) {
        Reaction.ReactionType reactionTypee = Reaction.ReactionType.valueOf(reactionType);
        Profile reactor = profileRepository.findById(reactorID)
                .orElseThrow(() -> new UserNotFoundException(reactorID));
        ReactableContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
        content.setNumOfReactions(content.getNumOfReactions() + 1);
        Reaction createdReaction = new Reaction(reactor, reactionTypee, content);
        return reactionRepository.save(createdReaction);

    }

    public boolean isReactor(String username, Long reactionID) {
        Optional<Reaction> reactionOptional = reactionRepository.findById(reactionID);
        if (reactionOptional.isPresent()) {
           Reaction reaction = reactionOptional.get();
            return reaction.getReactor().getUsername().equals(username);
        }
        return false; // Return false if the comment is not found
    }

}
