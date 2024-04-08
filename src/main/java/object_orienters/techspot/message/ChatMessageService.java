package object_orienters.techspot.message;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    @Autowired
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage saveChatMessage(ChatMessage chatMessage) throws ExecutionException, InterruptedException {
        String chatRoomId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        chatMessage.setChatRoomId(chatRoomId);
        return chatMessageRepository.saveChatMessage(chatMessage);
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        return chatRoomService.getChatRoomId(senderId, recipientId, false)
                .map(chatMessageRepository::findChatMessagesByChatRoomId)
                .orElse(new ArrayList<>());

    }

}
