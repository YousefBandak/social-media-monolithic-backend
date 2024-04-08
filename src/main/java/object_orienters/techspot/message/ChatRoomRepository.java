package object_orienters.techspot.message;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.checkerframework.checker.guieffect.qual.SafeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class ChatRoomRepository {

    private Firestore firestore;

    @Autowired
    public ChatRoomRepository(Firestore firestore) {
        this.firestore = firestore;
    }
    public ChatRoomRepository() {
       // this.firestore = FirestoreClient.getFirestore();
    }

    public Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId) throws ExecutionException, InterruptedException {

      //  return Optional.ofNullable(firestore.collection("chatRooms").document(senderId + recipientId).get().get().toObject(ChatRoom.class));
      return   Optional.empty();
    }

    public void saveChatRoom(ChatRoom chatRoom) {
       // firestore.collection("chatRooms").document(chatRoom.getSenderId() + chatRoom.getRecipientId()).set(chatRoom);
    }
}
