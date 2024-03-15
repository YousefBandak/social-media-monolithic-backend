package object_orienters.techspot.message;

import object_orienters.techspot.chat.ChatController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class MessageModelAssembler implements RepresentationModelAssembler<Message, EntityModel<Message>> {
    @Override
    public EntityModel<Message> toModel(Message entity) {
        return EntityModel.of(entity, //
                WebMvcLinkBuilder.linkTo(methodOn(MessageControler.class).getMessage(entity.getSender().getUsername(),entity.getChat().getChatId(),entity.getMessageId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(ChatController.class).getChat(entity.getSender().getUsername(),entity.getChat().getChatId())).withRel("chat"));
    }

    @Override
    public CollectionModel<EntityModel<Message>> toCollectionModel(Iterable<? extends Message> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
