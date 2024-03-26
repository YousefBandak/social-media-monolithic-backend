package object_orienters.techspot.comment;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import lombok.NoArgsConstructor;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.profile.Profile;

import java.sql.Timestamp;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ManyToAny;

@Data
@Entity
@Table(name = "comment")
@NoArgsConstructor

@Valid
public class Comment extends Content {

    @ManyToOne
    @JsonIgnore
    private Profile Commenter;

    @ManyToOne
    @JoinColumn(name = "commented_on")
    @JsonIgnore
    private Content commentedOn;
    @NotBlank(message = "Comment content cannot be empty")
    private String comment;
    private int numOfReactions;
    //Note: numOfReplies can be validated to set a limit
    private int numOfReplies;


    public Comment(String comment) {
        this.comment = comment;
    }

}
