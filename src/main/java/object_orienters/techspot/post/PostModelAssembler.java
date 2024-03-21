package object_orienters.techspot.post;

import object_orienters.techspot.comment.CommentController;
import object_orienters.techspot.reaction.Reaction;
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
    public EntityModel<Post> toModel(Post entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PostController.class).getPost(entity.getContentId())).withSelfRel(),
                linkTo(methodOn(ReactionController.class).getReactions(entity.getContentId())).withRel("reactions"),
                linkTo(methodOn(CommentController.class).getComments(entity.getContentId())).withRel("comments")
        );

    }

    @Override
    public CollectionModel<EntityModel<Post>> toCollectionModel(Iterable<? extends Post> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
