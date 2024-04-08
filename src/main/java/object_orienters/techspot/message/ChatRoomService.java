package object_orienters.techspot.message;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    @Autowired
    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createIfNotExist) {
        try {
            chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                    .map(ChatRoom::getId).or(() -> {
                        if (createIfNotExist) {
                            String chatId = createChatRoom(senderId, recipientId);
                            return Optional.of(chatId);
                        }
                        return Optional.empty();
                    });
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private String createChatRoom(String senderId, String recipientId) {
        String chatId = senderId + recipientId;
        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.saveChatRoom(senderRecipient);
        chatRoomRepository.saveChatRoom(recipientSender);

        return chatId;
    }

}



