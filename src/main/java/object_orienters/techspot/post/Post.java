package object_orienters.techspot.post;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import object_orienters.techspot.comment.Comment;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.reaction.Reaction;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "post")
@NoArgsConstructor
@Valid
public class Post extends Content {

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    @Getter
    private Profile author;
    @NotBlank(message = "Post content cannot be empty")
    private String content;
    private Privacy privacy;
    private int numOfComments;
    private int numOfLikes;

    //private int numOfShares;

    public Post(String content, Privacy privacy, Profile author){

        this.content = content;
        this.privacy = privacy;
        this.author = author;
    }


    public String toString(){
        return "Post{" +
                "contentId=" + getContentId() +
                ", author=" + author.getUsername() +
                ", content='" + content + '\'' +
                ", privacy=" + privacy +
                ", numOfComments=" + numOfComments +
                ", numOfLikes=" + numOfLikes +
                '}';
    }


}
