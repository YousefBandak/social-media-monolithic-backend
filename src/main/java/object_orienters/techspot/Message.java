package object_orienters.techspot;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Message")
public class Message {
    private @Id @GeneratedValue Long id;
    private User sender;
    // private ?? content (unknown datatype) 
    private boolean isSeen;
    private Chat chat;
}
