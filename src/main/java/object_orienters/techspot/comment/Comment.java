package object_orienters.techspot.comment;

import jakarta.persistence.*;
import lombok.Data;
import object_orienters.techspot.Content;
import object_orienters.techspot.profile.Profile;

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
