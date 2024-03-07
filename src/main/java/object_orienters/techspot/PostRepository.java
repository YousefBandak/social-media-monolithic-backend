package object_orienters.techspot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

public interface PostRepository extends JpaRepository<Post, Long> {
}
