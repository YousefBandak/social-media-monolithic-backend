package object_orienters.techspot.message;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(Long messageId) {
        super("Message not found with ID: " + messageId);
    }
}

