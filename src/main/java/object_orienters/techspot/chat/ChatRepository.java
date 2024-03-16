package object_orienters.techspot.chat;

import object_orienters.techspot.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository  extends JpaRepository<Chat, Long> {
}
