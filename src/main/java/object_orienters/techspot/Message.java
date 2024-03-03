package object_orienters.techspot;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Message")
public class Message {
    private User sender;
    // private ?? content (unknown datatype) 
    private boolean isSeen;
    private Chat chat;
}
