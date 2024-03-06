package object_orienters.techspot.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Post implements Content{
    @Id
    private String postId;

    @OneToOne
    private User author;
    private Timestamp timestamp;
    private String content;
    private Privacy privacy;
    private int numOfComments;
    private int numOfLikes;


}
