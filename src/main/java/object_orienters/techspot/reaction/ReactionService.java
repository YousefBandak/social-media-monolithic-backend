package object_orienters.techspot.reaction;

import object_orienters.techspot.post.PostNotFoundException;

import java.util.List;

public interface ReactionService {
    public Reaction createReaction(Reaction reaction, Long contentId);

    public Reaction getReaction(String reactionId) throws PostNotFoundException;

    public List<Reaction> getReactions(Long contentId);

    public Reaction updateReaction(String reactionId, Reaction reaction);

    public void deleteReaction(String reactionId);


}
