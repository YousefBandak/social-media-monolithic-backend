package object_orienters.techspot.message;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FirestoreChatterRepository {

    private final Firestore firestore;

    @Autowired
    ProfileRepository profileRepository;

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

    public List<Chatter> getChattersByFollowship(String username) throws ExecutionException, InterruptedException {
        System.out.println("FirestoreChatterRepository.getChattersByFollowship: username = " + username);
        List<Profile> followers = profileRepository.findFollowersByUserId(username);
        List<Profile> following = profileRepository.findFollowingByUserId(username);

        System.out.println("FirestoreChatterRepository.getChattersByFollowship: followers = " + followers);
        System.out.println("FirestoreChatterRepository.getChattersByFollowship: following = " + following);

        List<String> usernames = Stream.concat(followers.stream(), following.stream())
                .map(p -> p.getUsername())
                .distinct()
                .collect(Collectors.toList());

        System.out.println("FirestoreChatterRepository.getChattersByFollowship: usernames = " + usernames);
        if (usernames.isEmpty())
            return List.of();
        System.out.println("FirestoreChatterRepository.getChattersByFollowship: usernames = " + usernames);
        List<Chatter> c =  firestore.collection("Chatters")
                .whereIn("username", usernames)
                .get()
                .get()
                .toObjects(Chatter.class);
        System.out.println("FirestoreChatterRepository.getChattersByFollowship: c = " + c);
        return c;
    }
}
