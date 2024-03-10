package object_orienters.techspot.service;

import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.Message;
import object_orienters.techspot.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ImpleMessageService implements MessageService{
    MessageRepository messageRepository;
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
            return "Message not found.";
        }
    }

    @Override
    public Message getMessage(Long MessageId) {
        return null;
    }

    @Override
    public Chat getAllMessage(Long chatId) {
        return null;
    }
}
