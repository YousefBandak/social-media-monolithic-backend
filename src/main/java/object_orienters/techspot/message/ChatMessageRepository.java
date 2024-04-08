package object_orienters.techspot.message;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class ChatMessageRepository {

    private final Firestore firestore;

    @Autowired
    public ChatMessageRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public ChatMessage saveChatMessage(ChatMessage chatMessage) throws ExecutionException, InterruptedException {
        firestore.collection("ChatRooms").document(chatMessage.getRecipientId() + chatMessage.getSenderId())
                .collection("Messages")
                .add(chatMessage).get().get().get().toObject(ChatMessage.class);

        return firestore.collection("ChatRooms").document(chatMessage.getSenderId() + chatMessage.getRecipientId())
                .collection("Messages")
                .add(chatMessage).get().get().get().toObject(ChatMessage.class);
    }

    public List<ChatMessage> findChatMessagesByChatRoomId(String chatRoomId) {

        List<ChatMessage> chatMessages = new ArrayList<>();

        try {
            CollectionReference messagesCollectionRef = firestore.collection("ChatRooms")
                    .document(chatRoomId)
                    .collection("Messages");

            for (DocumentReference document : messagesCollectionRef.listDocuments()) {
                chatMessages.add(document.get().get().toObject(ChatMessage.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return chatMessages;

    }
}
