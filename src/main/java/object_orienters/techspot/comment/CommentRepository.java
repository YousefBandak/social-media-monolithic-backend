package object_orienters.techspot.comment;

import object_orienters.techspot.content.ReactableContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    Comment save(Comment comment);

    Optional<Comment> findByContentID(Long commentID);

    void delete(Comment comment);

    Page<Comment> findByCommentedOn(ReactableContent contentID, Pageable pageable);

}
