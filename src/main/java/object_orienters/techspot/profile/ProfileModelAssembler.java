package object_orienters.techspot.profile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProfileModelAssembler implements RepresentationModelAssembler<Profile, EntityModel<Profile>> {

  @Override
  public EntityModel<Profile> toModel(Profile user) throws UserNotFoundException {

    return EntityModel.of(user,
        linkTo(methodOn(ProfileController.class).one(user.getUsername())).withSelfRel(),
        linkTo(methodOn(ProfileController.class).Followers(user.getUsername())).withRel("followers"),
        linkTo(methodOn(ProfileController.class).Following(user.getUsername())).withRel("following"));
  }
}
