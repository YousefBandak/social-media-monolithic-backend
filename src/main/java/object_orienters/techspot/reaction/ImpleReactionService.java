package object_orienters.techspot.reaction;


import jakarta.persistence.EntityNotFoundException;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ContentRepository;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostNotFoundException;
import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpleReactionService implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final ContentRepository contentRepository;
    private final ProfileRepository profileRepository;
    public ImpleReactionService(ReactionRepository reactionRepository, ContentRepository contentRepository, ProfileRepository profileRepository) {

        this.reactionRepository = reactionRepository;
        this.contentRepository = contentRepository;
        this.profileRepository = profileRepository;
    }

//    @Override
//    public Reaction createReaction(Reaction reaction) {
//        if (reaction == null) {
//            throw new IllegalArgumentException("Reaction object cannot be null.");
//        } else {
//            Content content = contentRepository.findById(reaction.getContent().getContentId()).orElseThrow(() -> new ContentNotFoundException(reaction.getContent().getContentId()));
//            reaction.setContent(content);
//            content.getReactions().add(reaction);
//            return reactionRepository.save(reaction);
//        }
//
//    }

    @Override
    public Reaction getReaction(Long reactionId) throws ReactionNotFoundException {

        return reactionRepository.findById(reactionId).orElseThrow(() -> new ReactionNotFoundException(reactionId));
    }

    @Override
    public List<Reaction> getReactions(Long contentId) {
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new ContentNotFoundException(contentId));
        return content.getReactions();
    }

    @Override
    public Reaction updateReaction(Long reactionId, Reaction updatedReaction)  {
        Reaction existingReaction = reactionRepository.findById(reactionId).orElseThrow(() -> new ReactionNotFoundException(reactionId));
        existingReaction.setContent(updatedReaction.getContent());
        existingReaction.setType(updatedReaction.getType());
        existingReaction.setReactionId(updatedReaction.getReactionId());
        return reactionRepository.save(existingReaction);
    }

    @Override
    public void deleteReaction(Long reactionId) {
        Content content = reactionRepository.findById(reactionId).orElseThrow(() -> new ReactionNotFoundException(reactionId)).getContent();
        content.getReactions().removeIf(r -> r.getReactionId().equals(reactionId));
        reactionRepository.deleteById(reactionId);

    }
    @Override
    public Reaction createReaction(String reactorId, Reaction.ReactionType reactionType, Long contentId) {
        Profile reactor = profileRepository.findById(reactorId)
                .orElseThrow(() -> new UserNotFoundException(reactorId));
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));

        Reaction createdReaction =  new Reaction(reactor, reactionType, content);
        return reactionRepository.save(createdReaction);

    }


}
