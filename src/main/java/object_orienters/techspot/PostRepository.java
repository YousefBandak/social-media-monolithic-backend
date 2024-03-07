package object_orienters.techspot;

import object_orienters.techspot.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;

public interface PostRepository extends JpaRepository<Post, Long> {
    Collection<Post> findByUsername(String username);
}
