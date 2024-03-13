package object_orienters.techspot.controller.assemblers;

import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.Reaction;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ReactionModelAssembler implements RepresentationModelAssembler<Reaction, EntityModel<Reaction>> {
    @Override
    public EntityModel<Reaction> toModel(Reaction entity) {
        return null;
    }

    @Override
    public CollectionModel<EntityModel<Reaction>> toCollectionModel(Iterable<? extends Reaction> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
