package object_orienters.techspot.repository;


import object_orienters.techspot.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
