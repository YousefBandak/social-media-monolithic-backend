package object_orienters.techspot.repository;

import object_orienters.techspot.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, String> {
}
