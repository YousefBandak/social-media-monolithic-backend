package object_orienters.techspot.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactableContentRepository extends JpaRepository<ReactableContent, Long> {

}
