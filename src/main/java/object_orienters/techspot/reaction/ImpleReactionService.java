package object_orienters.techspot.reaction;


import object_orienters.techspot.content.Content;
import object_orienters.techspot.content.ContentNotFoundException;
import object_orienters.techspot.content.ContentRepository;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostNotFoundException;
import object_orienters.techspot.post.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpleReactionService implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final ContentRepository contentRepository;

    public ImpleReactionService(ReactionRepository reactionRepository, ContentRepository contentRepository, PostRepository postRepository) {

        this.reactionRepository = reactionRepository;
        this.contentRepository = contentRepository;
    }

    @Override
    public Reaction createReaction(Reaction reaction, Long contentId) {
        if (reaction == null) {
            throw new IllegalArgumentException("Reaction object cannot be null.");
        } else {
            Content content = contentRepository.findById(contentId).orElseThrow(() -> new ContentNotFoundException(contentId));
            reaction.setContent(content);
            content.getReactions().add(reaction);
            return reactionRepository.save(reaction);
        }

    }

    @Override
    public Reaction getReaction(String reactionId) throws ReactionNotFoundException {

        return reactionRepository.findById(reactionId).orElseThrow(() -> new ReactionNotFoundException(reactionId));
    }

    @Override
    public List<Reaction> getReactions(Long contentId) {
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new ContentNotFoundException(contentId));
        return content.getReactions();
    }

    @Override
    public Reaction updateReaction(String reactionId, Reaction updatedReaction)  {
        Reaction existingReaction = reactionRepository.findById(reactionId).orElseThrow(() -> new ReactionNotFoundException(reactionId));
        existingReaction.setContent(updatedReaction.getContent());
        existingReaction.setType(updatedReaction.getType());
        existingReaction.setReactionId(updatedReaction.getReactionId());
        return reactionRepository.save(existingReaction);
    }

    @Override
    public void deleteReaction(String reactionId) {
        Content content = reactionRepository.findById(reactionId).orElseThrow(() -> new ReactionNotFoundException(reactionId)).getContent();
        content.getReactions().removeIf(r -> r.getReactionId().equals(reactionId));
        reactionRepository.deleteById(reactionId);

    }


}
