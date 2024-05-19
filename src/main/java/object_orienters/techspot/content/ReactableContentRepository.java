package object_orienters.techspot.content;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactableContentRepository extends PagingAndSortingRepository<ReactableContent, Long> {
    ReactableContent save(ReactableContent reactableContent);

    Optional<ReactableContent> findByContentID(Long commentID);




}
