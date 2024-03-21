package object_orienters.techspot.post;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class SharedPostModelAssembler implements RepresentationModelAssembler<SharedPost, EntityModel<SharedPost>> {
    @Override
    public EntityModel<SharedPost> toModel(SharedPost entity) {
        return EntityModel.of(entity);
    }

    @Override
    public CollectionModel<EntityModel<SharedPost>> toCollectionModel(Iterable<? extends SharedPost> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
