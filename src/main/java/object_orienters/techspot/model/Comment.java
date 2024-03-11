package object_orienters.techspot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Comment implements Content {
    @Id
    private String commentId;
    @OneToOne
    private Profile Commenter;

    @OneToMany
    // @JoinColumn(name = "post_id")
    // @JsonIgnore
    private Content commentedOn;
    private String comment;
    private int numOfReactions;
    private int numOfReplies;
    private Timestamp timestamp;

}
