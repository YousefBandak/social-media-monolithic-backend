package object_orienters.techspot.post;

import object_orienters.techspot.profile.UserNotFoundException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {
    // private static final Logger log =
    // LoggerFactory.getLogger(LoadDatabase.class);
    private final PostModelAssembler assembler;
    private final PostService postService;

    PostController(PostModelAssembler assembler,
            PostService postService) {
        this.assembler = assembler;
        this.postService = postService;
    }

    @GetMapping("/profiles/{username}/posts")
    public CollectionModel<EntityModel<Post>> getTimelinePosts(@PathVariable String username)
            throws UserNotFoundException {
        return assembler.toCollectionModel(postService.getTimelinePosts(username));
    }

    @PostMapping("/profiles/{username}/posts")
    public EntityModel<Post> addTimelinePosts(@PathVariable String username, @RequestBody Post post)
            throws UserNotFoundException {
        return assembler.toModel(postService.addTimelinePosts(username, post));
    }

    @PutMapping("/profiles/{username}/posts/{postId}")
    public EntityModel<Post> editTimelinePost(@PathVariable String username, @PathVariable long postId,
            @RequestBody Post newPost)
            throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException {
        return assembler.toModel(postService.editTimelinePost(username, postId, newPost));

    }

    @DeleteMapping("/profiles/{username}/posts/{postId}")
    public void deleteTimelinePost(@PathVariable String username, @PathVariable long postId)
            throws UserNotFoundException, PostNotFoundException {
        postService.deleteTimelinePost(username, postId);

    }

    @GetMapping("/posts/{postId}")
    public EntityModel<Post> getPost(@PathVariable long postId) throws PostNotFoundException {
        return assembler.toModel(postService.getPost(postId));
    }

    //        //TODO: make sure the post has the same author as the user, otherwise anyone can edit any post and make themselves the author
    //        //TODO: Specify if post is shared or authored


}
