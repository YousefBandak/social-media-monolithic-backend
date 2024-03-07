package object_orienters.techspot.repository;

import object_orienters.techspot.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository  extends JpaRepository<Chat, Long> {
}
