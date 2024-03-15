package object_orienters.techspot.post;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import object_orienters.techspot.comment.Comment;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.PostBase;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.reaction.Reaction;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "post")
@NoArgsConstructor
public class Post  extends Content implements PostBase {
//    @Id
//    @Column(name = "post_id")
//    private long postId;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile author;
    private Timestamp timestamp;
    private String content;
    private Privacy privacy;
    private int numOfComments;
    private int numOfLikes;
    //private int numOfShares;

    public Post(String content) {
        this.content = content;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.privacy = Privacy.PUBLIC;
    }

    @Override
    public void like(Reaction reaction) {
        this.numOfLikes++;
    }

    @Override
    public void comment(Comment comment) {

    }

    @Override
    public void share(Profile sharer) {

    }

//    @Override
//    public void comment(Comment comment) {
//        this.numOfComments++;
//        getComments().add(comment);
//    }

//    @Override
//    public void share(Profile sharer) {
//        //SharedPost sharedPost = new SharedPost(this, sharer);
//        sharer.getSharedPosts().add(sharedPost);
//    }

    @Override
    public long getPostId() {
        return 0;
    }

    @Override
    public void editPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    @Override
    public void delete() {

    }
}
