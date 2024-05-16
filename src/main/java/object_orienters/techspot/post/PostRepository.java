package object_orienters.techspot.post;

import object_orienters.techspot.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Assuming Tag has a 'tagName' field, and you want to find Posts by tagName
    @Query(value = "SELECT * FROM posts WHERE tags LIKE %:tagName%", nativeQuery = true)
    List<Post> findByTagName(@Param("tagName") String tagName);

}

