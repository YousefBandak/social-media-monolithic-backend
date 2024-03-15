package object_orienters.techspot.reaction;

import object_orienters.techspot.Content;
import object_orienters.techspot.reaction.Reaction;
import object_orienters.techspot.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, String> {
    List<Reaction> findByContentReactedTo(Content content);
    Optional<Reaction> findByReactorAndContentReactedTo(Profile reactor, Content content);


}
