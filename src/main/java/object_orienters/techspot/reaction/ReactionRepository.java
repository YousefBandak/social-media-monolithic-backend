package object_orienters.techspot.reaction;

import object_orienters.techspot.content.ReactableContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends PagingAndSortingRepository<Reaction, String> {

    Page<Reaction> findByContent(ReactableContent content, Pageable pageable);

    Page<Reaction> findByContentAndType(ReactableContent content, Reaction.ReactionType type, Pageable pageable);

    void deleteByReactionID(String reactionID);

    Optional<Reaction> findByReactionID(String reactionID);

    Reaction save(Reaction reaction);

    void delete(Reaction reaction);

}
