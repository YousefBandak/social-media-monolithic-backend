package object_orienters.techspot.controller;

import object_orienters.techspot.model.Reaction;
import object_orienters.techspot.service.interfaces.ReactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class ReactionControler {
    private ReactionService reactionService;


//    @GetMapping("/{postId}/reactions")
//    public List<Reaction> getAllReactions(@PathVariable String postId) {return null;}
    @GetMapping("/posts/{postId}/reactions")
    public ResponseEntity<List<Reaction>> getAllReactionsForPost(@PathVariable String postId) {

//        Content post = contentService.getPostById(postId);
//
//        if (post == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        List<Reaction> allReactions = reactionService.getAllReactionsForPost(post);
//
//        return ResponseEntity.ok(allReactions);
        return null;
    }
    @GetMapping("/users/{userId}/posts/{postId}")
    public ResponseEntity<Reaction> getReactionForUserAndPost(@PathVariable String userName, @PathVariable String postId) {
//        User user = userService.getUserById(userName);
//        Content post = contentService.getPostById(postId);
//
//        if (user == null || post == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Optional<Reaction> reaction = reactionService.getReactionForUserAndPost(user, post);
//
//        return reaction.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
    return null;}


//    @PostMapping("/{postId}/reactions")
//    public ResponseEntity<String> createReaction(@PathVariable String postId, @RequestBody Reaction reaction) {
//
//        return ResponseEntity.ok("Created a new reaction for post with ID " + postId);
//    }
//
//    // PUT: Update a specific reaction for a post
//    @PutMapping("/{postId}/reactions/{reactionId}")
//    public ResponseEntity<String> updateReaction(@PathVariable String postId, @PathVariable String reactionId, @RequestBody enum reactionType) {
//        //reactionService.updateReactionContent(reactionId, updatedReactionContent);
//
//        return ResponseEntity.ok("Updated reaction with ID " + reactionId + " for post with ID " + postId);
//    }
//    @DeleteMapping("/{postId}/reactions/{reactionId}")
//    public ResponseEntity<String> deleteReaction(
//            @PathVariable String postId,
//            @PathVariable String reactionId
//    ) {
//        // Assuming you have a service method to delete the reaction by reactionId
//        reactionService.deleteReactionById(reactionId);
//
//        return ResponseEntity.ok("Deleted reaction with ID " + reactionId + " for post with ID " + postId);
//    }
}
