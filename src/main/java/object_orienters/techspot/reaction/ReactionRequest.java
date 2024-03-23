package object_orienters.techspot.reaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionRequest {
    private String reactorId;
    private Reaction.ReactionType reactionType;
}
