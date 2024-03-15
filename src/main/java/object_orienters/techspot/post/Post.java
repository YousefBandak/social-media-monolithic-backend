package object_orienters.techspot.post;


import jakarta.persistence.*;
import lombok.Data;
import object_orienters.techspot.comment.Comment;
import object_orienters.techspot.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Post implements Content {
    @Id
    private long postId;

    @OneToOne
    private Profile author;
    private Timestamp timestamp;
    private String content;
    private Privacy privacy;
    private int numOfComments;
    private int numOfLikes;
   // private int numOfShares;
   @OneToMany(mappedBy ="post", fetch = FetchType.EAGER)
   private List<Comment> comments;

}
