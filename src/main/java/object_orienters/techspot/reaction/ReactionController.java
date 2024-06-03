package object_orienters.techspot.reaction;

import jakarta.validation.Valid;
import object_orienters.techspot.exceptions.ContentNotFoundException;
import object_orienters.techspot.exceptions.ExceptionsResponse;
import object_orienters.techspot.exceptions.ReactionNotFoundException;
import object_orienters.techspot.utilities.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/content/{contentID}")
@CrossOrigin(origins = "http://localhost:3000")
public class ReactionController {
    private final ReactionService reactionService;
    private final ReactionModelAssembler assembler;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final Logger logger = LoggerFactory.getLogger(ReactionController.class);

    private final PermissionService permissionService;

    public ReactionController(ReactionService reactionService, ReactionModelAssembler assembler, PermissionService permissionService) {
        this.reactionService = reactionService;
        this.assembler = assembler;
        this.permissionService = permissionService;
    }

    @GetMapping("/reactions/{reactionType}/users")
    @PreAuthorize("@permissionService.canAccessPost(#contentID, authentication.principal.username)")
    public ResponseEntity<?> getReactionsByType(@PathVariable Long contentID,
                                                @PathVariable String reactionType,
                                                @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            logger.info(">>>>Retrieving Reactions... @ " + getTimestamp() + "<<<<");

            Page<Reaction> reactionList = reactionService.getReactionsByType(contentID, reactionType.toUpperCase(), pageNumber, pageSize);
            logger.info(">>>>Reactions Retrieved. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(reactionList.stream().map(assembler::toModel).collect(Collectors.toList()));
        } catch (ContentNotFoundException e) {
            logger.info(">>>>Error Occurred:  " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ExceptionsResponse.getErrorResponseEntity(e, HttpStatus.NOT_FOUND);

        }
    }


    @GetMapping("/reactions")
    @PreAuthorize("@permissionService.canAccessPost(#contentID, authentication.principal.username)")
    public ResponseEntity<?> getReactions(@PathVariable Long contentID,
                                          @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        try {
            logger.info(">>>>Retrieving Reactions... @ " + getTimestamp() + "<<<<");
            Page<Reaction> reactionList = reactionService.getReactions(contentID, pageNumber, pageSize);
            logger.info(">>>>Reactions Retrieved. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(reactionList.stream().map(assembler::toModel).collect(Collectors.toList()));
        } catch (ContentNotFoundException e) {
            logger.info(">>>>Error Occurred:  " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ExceptionsResponse.getErrorResponseEntity(e, HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping("/reactions")
    @PreAuthorize("@permissionService.canAccessPost(#contentID, authentication.principal.username)")
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
            return ExceptionsResponse.getErrorResponseEntity(e, HttpStatus.NOT_FOUND);

        }

    }
    @DeleteMapping("/reactions")
    public ResponseEntity<?> deleteReaction(@PathVariable Long contentID) {
        try {
            logger.info(">>>>Deleting Reaction... @ " + getTimestamp() + "<<<<");
            reactionService.deleteReaction(SecurityContextHolder.getContext().getAuthentication().getName()+contentID);
            logger.info(">>>>Reaction Deleted. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.noContent().build();
        } catch (ReactionNotFoundException e) {
            logger.info(">>>>Error Occurred:  " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ExceptionsResponse.getErrorResponseEntity(e, HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/reactions/{username}")
    public ResponseEntity<?> isReactor(@PathVariable Long contentID, @PathVariable String username) {
        try {
            logger.info(">>>>Checking if user is reactor... @ " + getTimestamp() + "<<<<");
            var isReactor = reactionService.isReactor(username, username + contentID);
            logger.info(">>>>User is reactor. @ " + getTimestamp() + "<<<<");
            return ResponseEntity.ok(isReactor);
        } catch (ContentNotFoundException e) {
            logger.info(">>>>Error Occurred:  " + e.getMessage() + " @ " + getTimestamp() + "<<<<");
            return ExceptionsResponse.getErrorResponseEntity(e, HttpStatus.NOT_FOUND);

        }
    }

    private static String getTimestamp() {
        return LocalDateTime.now().format(formatter) + " ";
    }
}