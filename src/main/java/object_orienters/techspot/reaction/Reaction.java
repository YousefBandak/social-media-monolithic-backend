package object_orienters.techspot.reaction;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import object_orienters.techspot.Content;
import object_orienters.techspot.profile.Profile;

@Entity
@Data
public class Reaction {
    @Id
    private String reactionId;
    @OneToOne
    private Profile reactor;
    ReactionType type;
    @OneToOne
    private Content contentReactedTo;


    enum ReactionType {
        LIKE, DISLIKE, LOVE, SUPPORT, HAHA;
    }
}
