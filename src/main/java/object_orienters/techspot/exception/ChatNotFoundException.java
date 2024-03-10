package object_orienters.techspot.exception;

public class ChatNotFoundException extends RuntimeException {

    public ChatNotFoundException(Long chatId) {
        super("Chat not found with ID: " + chatId);
    }
}

