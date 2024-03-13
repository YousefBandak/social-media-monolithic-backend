package object_orienters.techspot.service;

import object_orienters.techspot.exception.ChatAlreadyExistsException;
import object_orienters.techspot.exception.ChatNotFoundException;
import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.Profile;
import object_orienters.techspot.repository.ChatRepository;
import object_orienters.techspot.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
@Service
public class ImpleChatService implements ChatService {
    ChatRepository chatRepository;
    ProfileRepository userRepository;
    public ImpleChatService(ChatRepository chatRepository, ProfileRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Chat createChat(Chat chat) {
        Objects.requireNonNull(chat, "Chat cannot be null");
        Profile sender = chat.getSender();
        Profile receiver = chat.getReceiver();
        Objects.requireNonNull(sender, "Sender cannot be null");
        Objects.requireNonNull(receiver, "Receiver cannot be null");

        // Check if the chat already exists in the repository
        if (chatRepository.findById(chat.getChatId()).isPresent()) {
            throw new ChatAlreadyExistsException(chat.getChatId());
        }

        // Check if there is already a chat between the sender and receiver
        if (sender.getInbox().stream()
                .anyMatch(c -> c.getReceiver().getName().equals(receiver.getName()) || c.getSender().getName().equals(receiver.getName()))) {
            throw new ChatAlreadyExistsException(chat.getChatId());
        }
        return chatRepository.save(chat);
    }


    @Override
    public Chat getChat(Long chatId){
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));
    }

    @Override
    public Chat deleteChat(Long chatId) {
        if (chatRepository.existsById(chatId)) {
            Chat deletedChat = chatRepository.findById(chatId).get();
            chatRepository.deleteById(chatId);
            return deletedChat;
        } else {
            throw new ChatNotFoundException(chatId);
        }
    }

    @Override
    public Set<Chat> getAllChats(String userName) {
        Profile user = userRepository.findById(userName).orElseThrow(() -> new UserNotFoundException(userName));
        return user.getInbox();
    }

}
