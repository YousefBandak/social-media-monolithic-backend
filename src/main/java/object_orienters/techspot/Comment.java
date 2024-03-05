package object_orienters.techspot;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Comment implements Content {
    @Id
    private String commentId;
    @OneToOne
    private User Commenter;
    @OneToOne
    private Content commentedOn;
    private String comment;
    private int numOfReactions;
    private int numOfReplies;
    private Timestamp timestamp;

}
