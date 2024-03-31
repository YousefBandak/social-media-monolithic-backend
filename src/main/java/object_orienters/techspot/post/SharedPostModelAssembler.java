package object_orienters.techspot.post;

import lombok.NonNull;
import object_orienters.techspot.comment.CommentController;
import object_orienters.techspot.profile.ProfileController;
import object_orienters.techspot.reaction.ReactionController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SharedPostModelAssembler implements RepresentationModelAssembler<SharedPost, EntityModel<SharedPost>> {
    @Override
    @NonNull
    public EntityModel<SharedPost> toModel(@NonNull SharedPost entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PostController.class).getSharedPost(entity.getContentID(),entity.getSharer().getUsername())).withSelfRel(),
                linkTo(methodOn(PostController.class).getPost(entity.getPost().getContentID(),entity.getPost().getContentAuthor().getUsername())).withRel("originalPost"),
                linkTo(methodOn(ProfileController.class).one(entity.getSharer().getUsername())).withRel("sharer"),
                linkTo(methodOn(ProfileController.class).one(entity.getPost().getContentAuthor().getUsername())).withRel("author"));
    }
}
