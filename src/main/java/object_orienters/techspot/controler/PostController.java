package object_orienters.techspot.controler;


import object_orienters.techspot.PostModelAssembler;
import object_orienters.techspot.PostRepository;
import object_orienters.techspot.model.Post;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class PostController {
    //private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private final PostModelAssembler assembler;
    private final PostRepository postRepository;

    PostController(PostRepository postRepository, PostModelAssembler assembler) {
        this.postRepository = postRepository;
        this.assembler = assembler;
    }

    @GetMapping("/profiles/{username}/posts")
    public CollectionModel<EntityModel<Post>> getTimelinePosts(@PathVariable String username) {
//        List<EntityModel<Post>> posts = .stream()
//                .map(p -> assembler.toModel(p))
//                .collect(Collectors.toList());

        return assembler.toCollectionModel(postRepository.findByUsername(username));
    }
}
