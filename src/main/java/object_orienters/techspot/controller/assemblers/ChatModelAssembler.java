package object_orienters.techspot.controller.assemblers;

import object_orienters.techspot.controller.ChatController;
import object_orienters.techspot.controller.ProfileController;
import object_orienters.techspot.model.Chat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class ChatModelAssembler implements RepresentationModelAssembler<Chat, EntityModel<Chat>> {
    @Override
    public EntityModel<Chat> toModel(Chat entity) {
        return EntityModel.of(entity, //
                WebMvcLinkBuilder.linkTo(methodOn(ChatController.class).getChat(entity.getSender().getUsername(),entity.getChatId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(ProfileController.class).one(entity.getSender().getUsername())).withRel("sender"),
                linkTo(methodOn(ProfileController.class).one(entity.getReceiver().getUsername())).withRel("receiver"),
                linkTo(methodOn(ChatController.class).getChats(entity.getSender().getUsername())).withRel("chats"));
    }

    @Override
    public CollectionModel<EntityModel<Chat>> toCollectionModel(Iterable<? extends Chat> entities) {
        List<EntityModel<Chat>> chatModels = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(chatModels,
                linkTo(methodOn(ChatController.class).getChats("")).withSelfRel());
    }
}
