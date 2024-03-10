package object_orienters.techspot.exception;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(Long messageId) {
        super("Message not found with ID: " + messageId);
    }
}

