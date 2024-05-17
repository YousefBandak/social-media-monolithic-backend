package object_orienters.techspot.post;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    Optional<Post> findByContentID(Long Id);

    Post save(Post post);

    void delete(Post post);

    @Query("SELECT p FROM Post p WHERE p.tags LIKE CONCAT('%', :tagName, '%') AND p.privacy IN :privacies")
    Page<Post> findByTagNameAndPrivacy(@Param("tagName") String tagName, @Param("privacies") List<Privacy> privacies, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.contentAuthor IN :authors AND p.privacy IN :privacies")
    Page<Post> findPostsByContentAuthorsAndPrivacy(@Param("authors") List<Profile> authors, @Param("privacies") List<Privacy> privacies, Pageable pageable);

    List<Post> findAllByContentAuthorAndPrivacy(Profile author, Privacy privacy, Pageable pageable);
}
