package object_orienters.techspot.service;

import object_orienters.techspot.exception.ChatAlreadyExistsException;
import object_orienters.techspot.exception.ChatNotFoundException;
import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.User;
import object_orienters.techspot.repository.ChatRepository;
import object_orienters.techspot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
@Service
public class ImpleChatService implements ChatService {
    ChatRepository chatRepository;
    UserRepository userRepository;
    public ImpleChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Chat createChat(Chat chat) {
        Objects.requireNonNull(chat, "Chat cannot be null");
        User sender = chat.getSender();
        User receiver = chat.getReceiver();
        Objects.requireNonNull(sender, "Sender cannot be null");
        Objects.requireNonNull(receiver, "Receiver cannot be null");

        // Check if the chat already exists in the repository
        if (chatRepository.findById(chat.getChatId()).isPresent()) {
            throw new ChatAlreadyExistsException("Chat already exists in the repository");
        }

        // Check if there is already a chat between the sender and receiver
        if (sender.getInbox().stream()
                .anyMatch(c -> c.getReceiver().getName().equals(receiver.getName()) || c.getSender().getName().equals(receiver.getName()))) {
            throw new ChatAlreadyExistsException("Chat already exists between the sender and receiver");
        }
        return chatRepository.save(chat);
    }


    @Override
    public Chat getChat(Long chatId){
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Chat not found with ID: " + chatId));
    }

    @Override
    public String deleteChat(Long chatId) {
        if (chatRepository.existsById(chatId)) {
            chatRepository.deleteById(chatId);
            return "Chat with ID: " + chatId + " ,deleted successfully";
        } else {
            throw new ChatNotFoundException("Chat not found with ID: " + chatId);
        }
    }

    @Override
    public Set<Chat> getAllChats(String userName) {
        return null;
    }

    @Override
    public User getUserByUsername(String userName) throws UserNotFoundException {
        return userRepository.findById(userName).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
