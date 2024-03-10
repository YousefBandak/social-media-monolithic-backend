package object_orienters.techspot.exception;

public class ChatAlreadyExistsException extends RuntimeException {

    public ChatAlreadyExistsException(Long chatId) {
        super("Chat already exists with ID: " + chatId);
    }
}

