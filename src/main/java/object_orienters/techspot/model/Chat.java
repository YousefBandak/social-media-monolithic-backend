package object_orienters.techspot.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Chat")
public class Chat {
    @Id
    private Long chatId;
    private User sender;
    private User receiver;
    private List<Message> messages;
}
