package object_orienters.techspot;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Chat")
public class Chat {
    private User sender;
    private User receiver;
    private List<Message> messages;
}
