package object_orienters.techspot.message;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import object_orienters.techspot.chat.Chat;
import object_orienters.techspot.profile.Profile;

@Entity
@Data
@Table(name = "Message")
public class Message {
    @Id
    private Long messageId;
    private Profile sender;
    private String content;
    private boolean isSeen;
    private Chat chat;
}
