package object_orienters.techspot.reaction;

public class ReactionNotFoundException extends RuntimeException {
    public ReactionNotFoundException(Long reactionId) {
        super("Reaction not found with ID: " + reactionId);
    }

}
