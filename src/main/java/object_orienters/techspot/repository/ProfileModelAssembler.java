package object_orienters.techspot.repository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import object_orienters.techspot.controller.ProfileController;
import object_orienters.techspot.model.Profile;

@Component
public class ProfileModelAssembler implements RepresentationModelAssembler<Profile, EntityModel<Profile>> {

  @Override
  public EntityModel<Profile> toModel(Profile user) {

    return EntityModel.of(user, //
        linkTo(methodOn(ProfileController.class).one(user.getUsername())).withSelfRel());
        //linkTo(methodOn(ProfileController.class).followers(user.getUserName())).withRel("followers"));
  }
}
