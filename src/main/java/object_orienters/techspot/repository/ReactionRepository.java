package object_orienters.techspot.repository;

import object_orienters.techspot.model.Content;
import object_orienters.techspot.model.Reaction;
import object_orienters.techspot.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, String> {
    List<Reaction> findByContentReactedTo(Content content);
    Optional<Reaction> findByReactorAndContentReactedTo(Profile reactor, Content content);


}
