package object_orienters.techspot.message;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class ChatRoomRepository {

    private final Firestore firestore;

    @Autowired
    public ChatRoomRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public ChatRoomRepository() {
        this.firestore = FirestoreClient.getFirestore();
    }

    public Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId)
            throws ExecutionException, InterruptedException {

        return Optional.ofNullable(firestore.collection("ChatRooms").document(senderId + recipientId).get().get()
                .toObject(ChatRoom.class));
    }

    public void saveChatRoom(ChatRoom chatRoom) {
        if (chatRoom.getId() == null || chatRoom.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("ChatRoom id must not be null or empty");
        }
        @SuppressWarnings("unused")
        ApiFuture<WriteResult> collectionApiFuture = firestore.collection("ChatRooms").document(chatRoom.getId())
                .set(chatRoom);
    }

}
