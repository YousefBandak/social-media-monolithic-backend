package object_orienters.techspot.chat;

import object_orienters.techspot.profile.ProfileController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class ChatModelAssembler implements RepresentationModelAssembler<Chat, EntityModel<Chat>> {
    @Override
    public EntityModel<Chat> toModel(Chat entity) {
        return EntityModel.of(entity, //
                linkTo(methodOn(ChatController.class).getChat(entity.getSender().getUsername(),entity.getChatId())).withSelfRel(),
                linkTo(methodOn(ProfileController.class).one(entity.getSender().getUsername())).withRel("sender"),
                linkTo(methodOn(ProfileController.class).one(entity.getReceiver().getUsername())).withRel("receiver"),
                linkTo(methodOn(ChatController.class).getChats(entity.getSender().getUsername())).withRel("chats"));
    }

    @Override
    public CollectionModel<EntityModel<Chat>> toCollectionModel(Iterable<? extends Chat> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

}
