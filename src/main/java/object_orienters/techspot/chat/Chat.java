package object_orienters.techspot.chat;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import object_orienters.techspot.message.Message;
import object_orienters.techspot.profile.Profile;

@Entity
@Data
@Table(name = "Chat")
public class Chat {
    @Id
    private Long chatId;
    @OneToOne
    private Profile sender;
    @OneToOne

    private Profile receiver;
    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    private List<Message> messages;
}
