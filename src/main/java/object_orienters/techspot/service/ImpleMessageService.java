package object_orienters.techspot.service;

import object_orienters.techspot.exception.ChatNotFoundException;
import object_orienters.techspot.exception.MessageNotFoundException;
import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.Message;
import object_orienters.techspot.repository.ChatRepository;
import object_orienters.techspot.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImpleMessageService implements MessageService{
    MessageRepository messageRepository;
    ChatRepository chatRepository;
    @Override
    public Message createMessage(Message message) {
        Objects.requireNonNull(message, "message cannot be null");

        return messageRepository.save(message);
    }

    @Override
    public String deleteMessage(Long messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isPresent()) {
            messageRepository.deleteById(messageId);
            return "Message deleted successfully.";
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
