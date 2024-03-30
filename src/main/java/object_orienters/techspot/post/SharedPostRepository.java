package object_orienters.techspot.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedPostRepository extends JpaRepository<SharedPost, Long> {
}
