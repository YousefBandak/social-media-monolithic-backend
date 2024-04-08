package object_orienters.techspot.profile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import object_orienters.techspot.post.PostController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;

@Component
public class ProfileModelAssembler implements RepresentationModelAssembler<Profile, EntityModel<Profile>> {

    @SuppressWarnings("null")
    @Override
    @Nonnull
    public EntityModel<Profile> toModel(@Nonnull Profile user) throws UserNotFoundException {

        return EntityModel.of(user,
                linkTo(methodOn(ProfileController.class).one(user.getUsername())).withSelfRel(),
                linkTo(methodOn(ProfileController.class).Followers(user.getUsername())).withRel("followers"),
                linkTo(methodOn(ProfileController.class).Following(user.getUsername())).withRel("following"),
                linkTo(methodOn(PostController.class).getTimelinePosts(user.getUsername())).withRel("Posts"));
    }
}
