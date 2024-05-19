package object_orienters.techspot.exceptions;

public class ReactionNotFoundException extends RuntimeException {
    public ReactionNotFoundException(String reactionId) {
        super("Reaction not found with ID: " + reactionId);
    }

}
