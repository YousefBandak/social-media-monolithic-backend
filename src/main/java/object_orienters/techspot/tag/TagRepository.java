package object_orienters.techspot.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, String> {

    Optional<Tag> findByTagName(String tagName);

}

