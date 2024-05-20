package object_orienters.techspot.message;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class FirestoreChatterRepository {

    private final Firestore firestore;

    @Autowired
    public FirestoreChatterRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public String saveChatter(Chatter chatter) {
        ApiFuture<WriteResult> collectionApiFuture = firestore.collection("Chatters").document(chatter.getUsername())
                .set(chatter);
        return "Chatter saved";
    }

    public Optional<Chatter> getChatter(String username) throws ExecutionException, InterruptedException {
        return Optional
                .ofNullable(firestore.collection("Chatters").document(username).get().get().toObject(Chatter.class));
    }

    public List<Chatter> getChattersByStatus(Status status) throws ExecutionException, InterruptedException {
        return firestore.collection("Chatters").whereEqualTo("status", status).get().get().toObjects(Chatter.class);
    }
}
