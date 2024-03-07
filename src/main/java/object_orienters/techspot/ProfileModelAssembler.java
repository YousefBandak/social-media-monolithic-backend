package object_orienters.techspot;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProfileModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

  @Override
  public EntityModel<User> toModel(User user) {

    return EntityModel.of(user, //
        linkTo(methodOn(ProfileController.class).one(user.getUsername())).withSelfRel(),
        linkTo(methodOn(ProfileController.class).followers(user.getUsername())).withRel("followers"));
  }
}
