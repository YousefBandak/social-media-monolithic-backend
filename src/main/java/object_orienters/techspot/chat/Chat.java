package object_orienters.techspot.chat;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import object_orienters.techspot.message.Message;
import object_orienters.techspot.profile.Profile;

@Entity
@Data
@Table(name = "Chat")
public class Chat {
    @Id
    private Long chatId;
    private Profile sender;
    private Profile receiver;
    private List<Message> messages;
}
