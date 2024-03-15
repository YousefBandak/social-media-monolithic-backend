package object_orienters.techspot.chat;

public class ChatAlreadyExistsException extends RuntimeException {

    public ChatAlreadyExistsException(Long chatId) {
        super("Chat already exists with ID: " + chatId);
    }
}

