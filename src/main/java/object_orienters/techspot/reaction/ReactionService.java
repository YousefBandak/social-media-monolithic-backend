package object_orienters.techspot.reaction;

import object_orienters.techspot.post.PostNotFoundException;

import java.util.List;

public interface ReactionService {
    public Reaction createReaction(String reactorId, String reactionType, Long contentId);

    public Reaction getReaction(Long reactionId) throws PostNotFoundException;

    public List<Reaction> getReactions(Long contentId);

    public Reaction updateReaction(Long reactionId, String reactionType);

    public void deleteReaction(Long reactionId);

}
