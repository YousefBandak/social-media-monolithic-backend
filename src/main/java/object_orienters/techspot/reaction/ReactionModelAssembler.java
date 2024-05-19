package object_orienters.techspot.reaction;

import object_orienters.techspot.post.PostController;
import object_orienters.techspot.profile.ProfileController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReactionModelAssembler implements RepresentationModelAssembler<Reaction, EntityModel<Reaction>> {
        @Override
        @NonNull
        public EntityModel<Reaction> toModel(@NonNull Reaction entity) {
                return EntityModel.of(entity,
                                linkTo(methodOn(ProfileController.class).one(entity.getReactor().getUsername()))
                                                .withRel("reactor"),
                                linkTo(methodOn(PostController.class).getPost(entity.getContent().getContentID(),
                                                entity.getContent().getContentAuthor().getUsername())).withRel("post"),
                                linkTo(methodOn(ReactionController.class)
                                                .getReactions(entity.getContent().getContentID(), 0, 10))
                                                .withRel("reactions"));
        }

}
