package object_orienters.techspot.reaction;

import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.profile.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends PagingAndSortingRepository<Reaction, Long> {

    Page<Reaction> findByContent(ReactableContent content, Pageable pageable);

    void deleteByReactionID(Long reactionID);

    Optional<Reaction> findByReactionID(Long reactionID);

    Reaction save(Reaction reaction);

    void delete(Reaction reaction);

}
