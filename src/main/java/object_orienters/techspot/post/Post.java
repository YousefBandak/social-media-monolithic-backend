package object_orienters.techspot.post;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.profile.Profile;

@Entity
@Data
@Table(name = "post")
@NoArgsConstructor
@Valid
public class Post extends ReactableContent {

    // private String content;

    @Enumerated(EnumType.STRING)
    private Privacy privacy;
    private int numOfShares;
    private List<String> tags;

    public Post(DataType mediaData, Privacy privacy, Profile author) {
        this.setMediaData(mediaData);
        this.privacy = privacy;
        this.setContentAuthor(author);
        author.getPublishedPosts().add(this);
    }

    public Post(String textData, Privacy privacy, Profile author) {
        this.setTextData(textData);
        this.privacy = privacy;
        this.setContentAuthor(author);
    }

    public String toString() {
        return "Post{" +
                "contentId=" + getContentID() +
                ", author=" + this.getContentAuthor().getUsername() +
                ", content='" + this.getMediaData() + '\'' +
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
