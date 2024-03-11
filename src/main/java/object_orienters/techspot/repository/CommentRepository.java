package object_orienters.techspot.repository;

import object_orienters.techspot.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
