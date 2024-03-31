package object_orienters.techspot.content;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import object_orienters.techspot.comment.Comment;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.reaction.Reaction;

import java.sql.Timestamp;
import java.util.List;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = Post.class, name = "post"),
//        @JsonSubTypes.Type(value = Comment.class, name = "comment")
//})
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "ReactableContent")
@Data
public abstract class ReactableContent extends Content{

    @ManyToOne
    // @JsonBackReference
    private Profile contentAuthor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp timestamp;

    @JsonIgnore
    @OneToMany(mappedBy = "content", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions;

    @JsonIgnore
    @OneToMany(mappedBy = "commentedOn", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    private int numOfComments;
    private int numOfReactions;

    public ReactableContent() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Post))
            return false;
        return this.getContentID() != null && this.getContentID().equals(((Post) o).getContentID());
    }

    public Profile getContentAuthor() {
        return contentAuthor;
    }
}
