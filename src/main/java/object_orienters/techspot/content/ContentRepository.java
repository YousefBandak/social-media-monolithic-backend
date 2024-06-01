package object_orienters.techspot.content;

import object_orienters.techspot.model.ContentType;
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
import java.util.Set;

@Repository
public interface ContentRepository extends PagingAndSortingRepository<Content, Long> {

    Optional<Content> findByContentID(Long contentID);

    Content save(Content content);

    @Query("SELECT c FROM Content c WHERE c.contentAuthor = :author AND c.privacy IN :privacies AND c.contentType IN :contentTypes")
    Page<Content> findAllByMainAuthorAndContentTypeAndPrivacy(@Param("author") Profile author, @Param("privacies") List<Privacy> privacies, @Param("contentTypes") List<ContentType> contentTypes, Pageable pageable);

    @Query("SELECT c FROM Content c WHERE c.contentAuthor IN :authors AND c.privacy IN :privacies AND c.contentType IN :contentTypes")
    Page<Content> findAllByMainAuthorsAndContentTypeAndPrivacy(@Param("authors") Set<Profile> authors, @Param("privacies") List<Privacy> privacies, @Param("contentTypes") List<ContentType> contentTypes, Pageable pageable);


}
