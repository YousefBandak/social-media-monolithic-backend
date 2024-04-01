package object_orienters.techspot.comment;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import lombok.NoArgsConstructor;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;


import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "comment")
@NoArgsConstructor
@Valid
//@Data
public class Comment extends ReactableContent {

    @ManyToOne
    @JoinColumn(name = "commented_on")
    @JsonIgnore
    private ReactableContent commentedOn;
    @NotBlank(message = "Comment content cannot be empty")
    private String comment;
    private int numOfReactions;
    //Note: numOfReplies can be validated to set a limit
    private int numOfReplies;


    public Comment(String comment, Profile commentor, ReactableContent commentedOn) {
        this.comment = comment;
        this.setContentAuthor(commentor);
        this.commentedOn = commentedOn;
    }


    public ReactableContent getCommentedOn() {
        return commentedOn;
    }


    public void setCommentedOn(ReactableContent commentedOn) {
        this.commentedOn = commentedOn;
    }


    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
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
