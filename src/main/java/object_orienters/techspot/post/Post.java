package object_orienters.techspot.post;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    
    @Enumerated(EnumType.STRING)
    private Privacy privacy;
    private int numOfShares;

    public Post(String content, Privacy privacy, Profile author){

        this.content = content;
        this.privacy = privacy;
        this.setContentAuthor(author);
        author.getPublishedPosts().add(this);
    }


    public String toString(){
        return "Post{" +
                "contentId=" + getContentID() +
                ", author=" + this.getContentAuthor().getUsername() +
                ", content='" + content + '\'' +
                ", privacy=" + privacy +
                ", numOfComments=" + this.getNumOfComments() +
                ", numOfReactions=" + this.getNumOfReactions() +
                ", numOfShares=" + numOfShares +
                '}';
    }


    @Override
    public Profile getMainAuthor() {
        return this.getContentAuthor();
    }
}
