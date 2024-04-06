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

   // Collection<Post> findBySharer(Profile user); //TODO: add shared post implementation so that the user can see the shared posts
    Collection<Post> findByContentAuthor(Profile user);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t = :tag")
    List<Post> findByTag(@Param("tag") String tag);
}
