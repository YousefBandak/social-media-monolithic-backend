package object_orienters.techspot.reaction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.profile.Profile;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "reaction")
public class Reaction {
    @Id
    private String reactionID;

    @ManyToOne
    @NotNull(message = "Reactor profile should not be null.")
    private Profile reactor;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type shouldn't be null.")
    private ReactionType type;

    @ManyToOne
    @JoinColumn(name = "content_id")
    @JsonBackReference
    @NotNull(message = "Content should not be null.")
    private ReactableContent content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp timestamp;

    public Reaction(Profile reactor, ReactionType reactionType, ReactableContent content) {
        this.reactionID = reactor.getUsername() + content.getContentID();
        this.reactor = reactor;
        this.type = reactionType;
        this.content = content;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Reaction() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Reaction))
            return false;
        return reactionID != null && reactionID.equals(((Reaction) o).getReactionID());
    }

    public enum ReactionType {
        LIKE, DISLIKE, LOVE, SUPPORT, HAHA
    }

}
