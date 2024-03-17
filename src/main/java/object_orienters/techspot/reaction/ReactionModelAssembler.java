package object_orienters.techspot.reaction;

import object_orienters.techspot.profile.ProfileController;
import org.springframework.hateoas.CollectionModel;
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
//                return EntityModel.of(entity, //
//                linkTo(methodOn(ReactionController.class).get(entity.getSender().getUsername(),entity.getChatId())).withSelfRel(),
//                linkTo(methodOn(ProfileController.class).one(entity.getSender().getUsername())).withRel("sender"),
//                linkTo(methodOn(ProfileController.class).one(entity.getReceiver().getUsername())).withRel("receiver"),
//                linkTo(methodOn(ChatController.class).getChats(entity.getSender().getUsername())).withRel("chats"));
    return null;}

}
