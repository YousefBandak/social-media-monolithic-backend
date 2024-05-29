package object_orienters.techspot.profile;

import jakarta.annotation.Nonnull;
import object_orienters.techspot.exceptions.UserNotFoundException;
import object_orienters.techspot.post.PostController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProfileModelAssembler implements RepresentationModelAssembler<Profile, EntityModel<Profile>> {

    @SuppressWarnings("null")
    @Override
    @Nonnull
    public EntityModel<Profile> toModel(@Nonnull Profile user) throws UserNotFoundException {

        return EntityModel.of(user,
                linkTo(methodOn(ProfileController.class).one(user.getUsername())).withSelfRel(),
                linkTo(methodOn(ProfileController.class).Followers(user.getUsername(), 0, 10)).withRel("followers"),
                linkTo(methodOn(ProfileController.class).Following(user.getUsername(), 0, 10)).withRel("following"),
                linkTo(methodOn(PostController.class).getTimelinePosts(user.getUsername(), 0, 10)).withRel("Posts"));
    }
}
