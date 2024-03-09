package object_orienters.techspot.controler;


import object_orienters.techspot.PostModelAssembler;
import object_orienters.techspot.PostRepository;
import object_orienters.techspot.exception.PostNotFoundException;
import object_orienters.techspot.exception.PostUnrelatedToUserException;
import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Post;
import object_orienters.techspot.model.User;
import object_orienters.techspot.repository.ProfileRepo;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {
    //private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private final PostModelAssembler assembler;
    private final PostRepository postRepository;
    private final ProfileRepo profileRepository;

    PostController(PostRepository postRepository, ProfileRepo profileRepository, PostModelAssembler assembler) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.assembler = assembler;
    }

    @GetMapping("/profiles/{username}/posts")
    public CollectionModel<EntityModel<Post>> getTimelinePosts(@PathVariable String username) throws UserNotFoundException {

        return assembler.toCollectionModel(postRepository.findByAuthor(profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username))));
    }

    @PostMapping("/profiles/{username}/posts")
    public EntityModel<Post> addTimelinePosts(@PathVariable String username, @RequestBody Post post) throws UserNotFoundException {
        User user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        post.setAuthor(user);
        postRepository.save(post);

        //TODO: Specify if post is shared or authored
        user.getPublishedPosts().add(post);
        profileRepository.save(user);
        return assembler.toModel(post);

    }

    @PutMapping("/profiles/{username}/posts/{postId}")
    public EntityModel<Post> editTimelinePost(@PathVariable String username, @PathVariable long postId, @RequestBody Post newPost) throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException {
        User user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        if ((!post.getAuthor().equals(user) || !user.getPublishedPosts().contains(post)) && !user.getSharedPosts().contains(post)) {
            throw new PostUnrelatedToUserException(username, postId);
        }

        //TODO: make sure the post has the same author as the user, otherwise anyone can edit any post and make themselves the author

        // post.setAuthor(user);
        post.setContent(newPost.getContent());
        post.setPrivacy(newPost.getPrivacy());

        postRepository.save(post);

        //TODO: Specify if post is shared or authored
        user.getPublishedPosts().add(post);
        profileRepository.save(user);
        return assembler.toModel(post);

    }


    @DeleteMapping("/profiles/{username}/posts/{postId}")
    public void deleteTimelinePost(@PathVariable String username, @PathVariable long postId) throws UserNotFoundException, PostNotFoundException {
        User user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        //TODO: Maybe we should mark the post for deletion instead of deleting it immediately
        //TODO: Do we need to update any references to that post before deleting it?
        postRepository.delete(post);


    }

    @GetMapping("/posts/{postId}")
    public EntityModel<Post> getPost(@PathVariable long postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return assembler.toModel(post);
    }


}
