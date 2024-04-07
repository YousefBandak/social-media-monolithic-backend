package object_orienters.techspot.message;


import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class ChatMessageRepository {

    private final Firestore firestore;

    @Autowired
    public ChatMessageRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        firestore.collection("chatMessages").document(chatMessage.getId()+"").set(chatMessage);
        System.out.println("Chat message saved");
        return chatMessage;
    }

    public List<ChatMessage> findChatMessagesByChatRoomId(String chatRoomId){
        try {
            return Collections.singletonList(firestore.collection("chatMessages").document(chatRoomId).get().get().toObject(ChatMessage.class));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
