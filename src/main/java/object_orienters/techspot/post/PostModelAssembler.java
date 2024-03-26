package object_orienters.techspot.post;

import io.micrometer.common.lang.NonNull;
import object_orienters.techspot.comment.CommentController;
import object_orienters.techspot.reaction.ReactionController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {
    @Override
    @NonNull
    public EntityModel<Post> toModel(@NonNull Post entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PostController.class).getPost(entity.getContentID())).withSelfRel(),
                linkTo(methodOn(ReactionController.class).getReactions(entity.getContentID())).withRel("reactions"),
                linkTo(methodOn(CommentController.class).getComments(entity.getContentID())).withRel("comments")
        );

    }
}
