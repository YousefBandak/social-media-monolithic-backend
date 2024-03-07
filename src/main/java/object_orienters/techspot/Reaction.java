package object_orienters.techspot;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Reaction {
    @Id
    private String reactionId;
    @OneToOne
    private User reactor;
    ReactionType type;
    @OneToOne
    private Content contentReactedTo;


    enum ReactionType {
        LIKE, DISLIKE, LOVE, SUPPORT, HAHA;
    }
}
