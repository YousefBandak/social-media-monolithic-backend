// In case remove the reaction when update the type What should the type be????
// Like the reaction was a like, how to remove it atoll (REMOVE THE REACTION).

package object_orienters.techspot.reaction;

import jakarta.validation.Valid;
import object_orienters.techspot.content.ContentNotFoundException;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/content/{contentID}")
public class ReactionController {
    private final ImpleReactionService reactionService;
    private final ReactionModelAssembler assembler;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final Logger logger = LoggerFactory.getLogger(ReactionController.class);

    public ReactionController(ImpleReactionService reactionService, ReactionModelAssembler assembler) {
        this.reactionService = reactionService;
        this.assembler = assembler;
    }

    @GetMapping("/reactions/{reactionId}")
    public ResponseEntity<?> getReaction(@PathVariable Long reactionId, @PathVariable Long contentID) {

        try {
            logger.info(">>>>Adding Reaction to Post... @ " + getTimestamp() + "<<<<");
            Reaction reaction = reactionService.getReaction(reactionId);
            EntityModel<Reaction> reactionModel = assembler.toModel(reaction);
            logger.info(">>>>Reaction Added to Post. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(reactionModel);
        } catch (ReactionNotFoundException | ContentNotFoundException e) {
            logger.info(">>>>Error Occurred:  " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @GetMapping("/reactions")
    public ResponseEntity<?> getReactions(@PathVariable Long contentID) {
        try {
            logger.info(">>>>Retrieving Reactions... @ " + getTimestamp() + "<<<<");
            List<Reaction> reactionList = reactionService.getReactions(contentID);
            logger.info(">>>>Reactions Retrieved. @ " + getTimestamp() + "<<<<");
            // CollectionModel<EntityModel<Reaction>> reactionModel =
            // CollectionModel.of(reactionList.stream().map(assembler::toModel).collect(Collectors.toList()),
            // linkTo(methodOn(ReactionController.class).getReactions(contentID)).withSelfRel(),
            // linkTo(methodOn(PostController.class).getPost(contentID)).withRel("post"));
            return ResponseEntity.ok(reactionList.stream().map(assembler::toModel).collect(Collectors.toList()));
        } catch (ContentNotFoundException e) {
            logger.info(">>>>Error Occurred:  " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @PostMapping("/reactions")
    public ResponseEntity<?> createReaction(@Valid @RequestBody Map<String, String> reaction,
            @PathVariable Long contentID) {
        try {
            logger.info(">>>>Adding Reaction... @ " + getTimestamp() + "<<<<");
            Reaction createdReaction = reactionService.createReaction(reaction.get("reactorID"),
                    reaction.get("reactionType"), contentID);
            EntityModel<Reaction> reactionModel = assembler.toModel(createdReaction);
            logger.info(">>>>Reaction Added. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.CREATED).body(reactionModel);
        } catch (IllegalArgumentException | ContentNotFoundException e) {
            logger.info(">>>>Error Occurred:  " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }

    }

    @PutMapping("/reactions/{reactionId}")
    @PreAuthorize("@impleReactionService.isReactor(authentication.principal.username, #reactionId)")
    public ResponseEntity<?> updateReaction(@PathVariable Long reactionId,
            @Valid @RequestBody Map<String, String> newReaction, @PathVariable Long contentID) {
        try {
            logger.info(">>>>Updating Reaction... @ " + getTimestamp() + "<<<<");
            Reaction reaction = reactionService.updateReaction(reactionId, newReaction.get("reactionType"));
            EntityModel<Reaction> reactionModel = assembler.toModel(reaction);
            logger.info(">>>>Reaction Updated. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(reactionModel);
        } catch (ReactionNotFoundException e) {
            logger.info(">>>>Error Occurred:  " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @DeleteMapping("/reactions/{reactionId}")
    @PreAuthorize("reactionService.isReactor(authentication.principal.username, #reactionId)")
    public ResponseEntity<?> deleteReaction(@PathVariable Long reactionId) {
        try {
            logger.info(">>>>Deleting Reaction... @ " + getTimestamp() + "<<<<");
            reactionService.deleteReaction(reactionId);
            logger.info(">>>>Reaction Deleted. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok("Reaction deleted successfully");
        } catch (ReactionNotFoundException e) {
            logger.info(">>>>Error Occurred:  " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }


    private static String getTimestamp() {
        return LocalDateTime.now().format(formatter) + " ";
    }
}