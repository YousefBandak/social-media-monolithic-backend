package object_orienters.techspot.reaction;



import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.profile.Profile;


@Entity
@Data
@NoArgsConstructor
@Table(name = "reaction")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;
    //@JsonIgnore
    @OneToOne
    @NotNull(message = "Reactor profile should not be null.")
    private Profile reactor;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type shouldn't be null.")
    private ReactionType type;
    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "content_id")
    @JsonBackReference
    @NotNull(message = "Content should not be null.")
    private Content content;
    public Reaction( Profile reactor, ReactionType reactionType,Content content) {
        this.reactor = reactor;
        this.type = reactionType;
        this.content = content;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reaction)) return false;
        return reactionId != null && reactionId.equals(((Reaction) o).getReactionId());
    }

    public enum ReactionType {

        LIKE, DISLIKE, LOVE, SUPPORT, HAHA;
    }

}
