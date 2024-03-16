package object_orienters.techspot.message;

import object_orienters.techspot.chat.ChatNotFoundException;
import object_orienters.techspot.chat.Chat;
import object_orienters.techspot.chat.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImpleMessageService implements MessageService {
    MessageRepository messageRepository;
    ChatRepository chatRepository;
    @Override
    public Message createMessage(Message message) {
        Objects.requireNonNull(message, "message cannot be null");

        return messageRepository.save(message);
    }

    @Override
    public Message deleteMessage(Long messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isPresent()) {
            Message message = messageRepository.findById(messageId).get();
            messageRepository.deleteById(messageId);
            return message;
        } else {
            throw new MessageNotFoundException(messageId);
        }
    }

    @Override
    public Message getMessage(Long messageId) {

        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        return optionalMessage.orElseThrow(() -> new MessageNotFoundException(messageId));
    }

    @Override
    public List<Message> getAllMessage(Long chatId) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        Chat chat = optionalChat.orElseThrow(() -> new ChatNotFoundException(chatId));

        return chat.getMessages();
    }
}
