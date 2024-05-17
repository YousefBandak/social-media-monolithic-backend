package object_orienters.techspot.content;

import object_orienters.techspot.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactableContentRepository extends PagingAndSortingRepository<ReactableContent, Long> {
    ReactableContent save(ReactableContent reactableContent);

    Optional<ReactableContent> findByContentID(Long commentID);



}
