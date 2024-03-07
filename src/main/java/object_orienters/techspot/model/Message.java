package object_orienters.techspot.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Message")
public class Message {
    @Id
    private Long messageId;
    private User sender;
    // private ?? content (unknown datatype) 
    private boolean isSeen;
    private Chat chat;
}
