package object_orienters.techspot.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.profile.Profile;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@Valid
// @Data
public class Comment extends ReactableContent {

    @ManyToOne
    @JoinColumn(name = "commented_on")
    @JsonIgnore
    private ReactableContent commentedOn;
    private int numOfReactions;
    private int numOfReplies;

    public Comment(DataType comment, Profile commentor, ReactableContent commentedOn) {
        this.setMediaData(comment);
        this.setContentAuthor(commentor);
        this.commentedOn = commentedOn;
        commentedOn.getComments().add(this);

    }

    public Comment(DataType comment, Profile commentor, ReactableContent commentedOn, String text) {
        this.setMediaData(comment);
        this.setContentAuthor(commentor);
        this.setTextData(text);
        this.commentedOn = commentedOn;
    }

    public Comment(Profile commentor, ReactableContent commentedOn, String text) {
        this.setContentAuthor(commentor);
        this.setTextData(text);
        this.commentedOn = commentedOn;

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
    public Profile getMainAuthor() {
        return this.getContentAuthor();
    }
}
