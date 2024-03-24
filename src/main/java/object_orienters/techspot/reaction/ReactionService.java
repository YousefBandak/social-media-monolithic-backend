package object_orienters.techspot.reaction;

import object_orienters.techspot.post.PostNotFoundException;

import java.util.List;

public interface ReactionService {
    //public Reaction createReaction(Reaction reaction);

    public Reaction getReaction(Long reactionId) throws PostNotFoundException;

    public List<Reaction> getReactions(Long contentId);

    public Reaction updateReaction(Long reactionId, Reaction reaction);

    public void deleteReaction(Long reactionId);
    public Reaction createReaction(String reactorId, Reaction.ReactionType reactionType, Long contentId);



}
