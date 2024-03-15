package object_orienters.techspot.comment;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;


public class CommentModelAssembler implements RepresentationModelAssembler<Comment, EntityModel<Comment>>{
    @Override
    @NonNull
    public EntityModel<Comment> toModel(@NonNull Comment entity) {
        return EntityModel.of(entity);
    }

    @Override
    @NonNull
    public CollectionModel<EntityModel<Comment>> toCollectionModel(Iterable<? extends Comment> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
