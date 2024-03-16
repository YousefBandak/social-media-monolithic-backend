package object_orienters.techspot.message;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import object_orienters.techspot.chat.Chat;
import object_orienters.techspot.profile.Profile;

@Entity
@Data
@Table(name = "Message")
public class Message {
    @Id
    private Long messageId;
    @OneToOne
    private Profile sender;
    private String content;
    private boolean isSeen;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    @JsonBackReference
    private Chat chat;
}
