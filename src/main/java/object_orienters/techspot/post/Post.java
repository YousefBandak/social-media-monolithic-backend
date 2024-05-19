package object_orienters.techspot.post;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.model.ContentType;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.profile.Profile;


import java.util.List;


@Entity
@Data
@Table(name = "post")
@Valid
public class Post extends ReactableContent {


    private int numOfShares;

    private String tags;


    public Post() {
        this.setContentType(ContentType.Post);
    }

    public String toString() {
        return "Post{" +
                "contentId=" + getContentID() +
                ", author=" + this.getContentAuthor().getUsername() +
                ", content='" + this.getMediaData() + '\'' +
                ", privacy=" + this.getPrivacy() +
                ", numOfComments=" + this.getNumOfComments() +
                ", numOfReactions=" + this.getNumOfReactions() +
                ", numOfShares=" + numOfShares +
                '}';

    }


    @Override
    public ContentType getContentType() {
        return ContentType.Post;
    }

}
