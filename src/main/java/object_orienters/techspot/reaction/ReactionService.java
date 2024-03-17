package object_orienters.techspot.reaction;

import java.util.List;

public interface ReactionService {
    public Reaction createReaction(Reaction reaction);
    public Reaction getReaction(String reactionId);
    public String deleteReaction(String reactionId);
    public List<Reaction> getReactions(Long contentId );
}
