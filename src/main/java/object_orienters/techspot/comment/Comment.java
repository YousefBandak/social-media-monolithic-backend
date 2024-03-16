package object_orienters.techspot.comment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.profile.Profile;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "comment")
@NoArgsConstructor

public class Comment extends Content {
        //    @Id
        //    private String commentId;
    @OneToOne
    private Profile Commenter;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    private Content commentedOn;
    private String comment;
    private int numOfReactions;
    private int numOfReplies;
    private Timestamp timestamp;

    public Comment(String comment) {
        this.comment = comment;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

}
