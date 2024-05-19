package object_orienters.techspot.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.model.ContentType;
import object_orienters.techspot.model.Privacy;

@SuppressWarnings("ALL")
@Entity
@Table(name = "comment")
@Valid
public class Comment extends ReactableContent {

    @ManyToOne
    @JoinColumn(name = "commented_on")
    @JsonIgnore
    private ReactableContent commentedOn;
    private int numOfReactions;
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

    @Override
    public Privacy getPrivacy() {
        return commentedOn.getPrivacy();
    }

    @Override
    public ContentType getContentType() {
        return ContentType.Comment;
    }
}
