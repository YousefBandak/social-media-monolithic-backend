package object_orienters.techspot.content;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import object_orienters.techspot.comment.Comment;
import object_orienters.techspot.post.Post;


import java.sql.Timestamp;


//NOTE: are these annptations still valid?
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = Post.class, name = "post"),
//        @JsonSubTypes.Type(value = Comment.class, name = "comment")
//})
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "content")
@Data
public abstract class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_id", updatable = false, nullable = false)
    @Getter
    private Long contentID;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp timestamp;

    public Content() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

}
