package object_orienters.techspot.post;

import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class SharedPostModelAssembler implements RepresentationModelAssembler<SharedPost, EntityModel<SharedPost>> {
    @Override
    @NonNull
    public EntityModel<SharedPost> toModel(@NonNull SharedPost entity) {
        return EntityModel.of(entity);
    }
}
