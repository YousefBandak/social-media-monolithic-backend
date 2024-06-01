package object_orienters.techspot.message;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ChatterService {

    @Autowired
    private FirestoreChatterRepository firestoreRepository;

    public void saveChatter(Chatter chatter) {
        chatter.setStatus(Status.ONLINE);
        firestoreRepository.saveChatter(chatter);
    }

    public void disconnectChatter(Chatter chatter) {
        try {
            Chatter storedChatter = firestoreRepository.getChatter(chatter.getUsername())
                    .orElseThrow(() -> new RuntimeException("Chatter not found"));
            storedChatter.setStatus(Status.OFFLINE);
            firestoreRepository.saveChatter(storedChatter);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        chatter.setStatus(Status.OFFLINE);
        firestoreRepository.saveChatter(chatter);
    }

    public List<Chatter> getConnectedChatters(String username) {
        try {
            System.out.println("ChatterService.getConnectedChatters username = " + username);
            return firestoreRepository.getChattersByFollowship(username);
            //return firestoreRepository.getChattersByStatus(Status.ONLINE);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
