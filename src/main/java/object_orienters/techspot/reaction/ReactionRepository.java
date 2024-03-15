package object_orienters.techspot.reaction;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, String> {
    List<Reaction> findByContent(Content content);
    Optional<Reaction> findByReactorAndContent(Profile reactor, Content content);


}
