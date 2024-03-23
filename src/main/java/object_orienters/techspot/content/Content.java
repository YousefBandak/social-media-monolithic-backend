package object_orienters.techspot.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import object_orienters.techspot.comment.Comment;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.reaction.Reaction;

import java.sql.Timestamp;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Post.class, name = "post"),
        @JsonSubTypes.Type(value = Comment.class, name = "comment")
})
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "content")
@Data

public abstract class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_id", updatable = false, nullable = false)
    @Getter
    private Long contentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp timestamp;

    @JsonIgnore
    @OneToMany(mappedBy = "content", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Reaction> reactions;

    @JsonIgnore
    @OneToMany(mappedBy = "commentedOn", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Content() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        return contentId != null && contentId.equals(((Post) o).getContentId());
    }
}
