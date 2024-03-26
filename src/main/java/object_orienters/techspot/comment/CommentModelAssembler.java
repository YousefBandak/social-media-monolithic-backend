package object_orienters.techspot.comment;

import object_orienters.techspot.post.PostController;
import object_orienters.techspot.profile.ProfileController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;

import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class CommentModelAssembler implements RepresentationModelAssembler<Comment, EntityModel<Comment>> {
    @Override
    @NonNull
    public EntityModel<Comment> toModel(@NonNull Comment entity) {
        EntityModel<Comment> commentModel = EntityModel.of(entity,
                linkTo(methodOn(CommentController.class).getComment(entity.getContentId())).withSelfRel(),
                linkTo(methodOn(PostController.class).getPost(entity.getCommentedOn().getContentId())).withRel("post"),
                linkTo(methodOn(ProfileController.class).one(entity.getCommenter().getUsername())).withRel("commenter"));

        if (entity.getComments() != null && !entity.getComments().isEmpty())
            commentModel.add(linkTo(methodOn(CommentController.class).getComments(entity.getContentId())).withRel("comments"));


        return commentModel;

    }
}
