package object_orienters.techspot.comment;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.model.ContentType;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.profile.Profile;

@Entity
@Table(name = "comment")
@Valid
// @Data
public class Comment extends ReactableContent {

    @ManyToOne
    @JoinColumn(name = "commented_on")
    @JsonIgnore
    private ReactableContent commentedOn;
    private int numOfReactions;
    private int numOfReplies;

    public Comment() {
        this.setContentType(ContentType.Comment);
    }

    public ReactableContent getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(ReactableContent commentedOn) {
        this.commentedOn = commentedOn;
    }

    public int getNumOfReactions() {
        return numOfReactions;
    }

    public void setNumOfReactions(int numOfReactions) {
        this.numOfReactions = numOfReactions;
    }

    public int getNumOfReplies() {
        return numOfReplies;
    }

    public void setNumOfReplies(int numOfReplies) {
        this.numOfReplies = numOfReplies;
    }

    @Override
    public Privacy getPrivacy() {
        return commentedOn.getPrivacy();
    }

    @Override
    public ContentType getContentType() {
        return ContentType.Comment;
    }
}
