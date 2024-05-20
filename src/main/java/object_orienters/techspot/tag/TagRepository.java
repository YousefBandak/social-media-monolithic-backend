package object_orienters.techspot.tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    Optional<Tag> findByTagName(String tagName);

    @Query("SELECT t FROM Tag t ORDER BY LENGTH(t.posts) DESC")
    Page<Tag> findMostPopularTags(Pageable pageable);

}

