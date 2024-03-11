package object_orienters.techspot.model;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Post extends Content{
    @Id
    private long postId;

    @OneToOne
    private User author;
    private Timestamp timestamp;
    private String content;
    private Privacy privacy;
    private int numOfComments;
    private int numOfLikes;
   // private int numOfShares;
   @OneToMany(mappedBy ="post", fetch = FetchType.EAGER)
   private List<Comment> comments;

}
