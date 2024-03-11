package object_orienters.techspot.repository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import object_orienters.techspot.controller.ProfileController;
import object_orienters.techspot.model.User;

@Component
public class ProfileModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

  @Override
  public EntityModel<User> toModel(User user) {

    return EntityModel.of(user, //
        linkTo(methodOn(ProfileController.class).one(user.getUserName())).withSelfRel());
        //linkTo(methodOn(ProfileController.class).followers(user.getUserName())).withRel("followers"));
  }
}
