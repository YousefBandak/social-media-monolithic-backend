package object_orienters.techspot.post;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;

@Entity
@Data
@Table(name = "post")
@NoArgsConstructor
@Valid
public class Post extends ReactableContent {

    @NotBlank(message = "Post content cannot be empty")
    private String content;
    private Privacy privacy;
    //TODO: handel this to be incremented when a comment is added
    private int numOfComments;
    //TODO: handel this to be incremented when a Reaction is added
    private int numOfReactions;
    private int numOfShares;

    public Post(String content, Privacy privacy, Profile author){

        this.content = content;
        this.privacy = privacy;
        this.setContentAuthor(author);
    }


    public String toString(){
        return "Post{" +
                "contentId=" + getContentID() +
                ", author=" + this.getContentAuthor().getUsername() +
                ", content='" + content + '\'' +
                ", privacy=" + privacy +
                ", numOfComments=" + numOfComments +
                ", numOfLikes=" + numOfReactions +
                '}';
    }


}
