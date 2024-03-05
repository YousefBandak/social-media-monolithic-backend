package object_orienters.techspot;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Reaction {
    @Id
    private String reactionId;
    @OneToOne
    User reactor;
    ReactionType type;
    @OneToOne
    Content contentReactedTo;


    enum ReactionType {
        LIKE, DISLIKE, LOVE, SUPPORT, HAHA;
    }
}
