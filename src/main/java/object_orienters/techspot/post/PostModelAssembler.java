package object_orienters.techspot.post;

import io.micrometer.common.lang.NonNull;
import object_orienters.techspot.comment.CommentController;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.ContentType;
import object_orienters.techspot.profile.ProfileController;
import object_orienters.techspot.reaction.ReactionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostModelAssembler implements RepresentationModelAssembler<Content, EntityModel<Content>> {

    @Override
    @NonNull
    public EntityModel<Content> toModel(@NonNull Content entity) {
        EntityModel e = EntityModel.of(entity,
                linkTo(methodOn(PostController.class).getPost(entity.getContentID(),
                        entity.getMainAuthor().getUsername())).withSelfRel(),
                linkTo(methodOn(ProfileController.class).one(entity.getMainAuthor().getUsername())).withRel("author"),
                linkTo(methodOn(ReactionController.class).getReactions(entity.getContentID(), 0, 10)).withRel("reactions"),
                linkTo(methodOn(CommentController.class).getComments(entity.getContentID(), 0, 10)).withRel("comments"),
                linkTo(methodOn(PostController.class).deleteTimelinePost(entity.getMainAuthor().getUsername(), entity.getContentID())).withRel("delete"),
                linkTo(methodOn(ReactionController.class).deleteReaction(entity.getContentID())).withRel("deleteReaction")
        );
        if(entity.getContentType() == ContentType.SharedPost){
            SharedPost sharedPost = (SharedPost) entity;
            e.add(linkTo(methodOn(ReactionController.class).getReactions(sharedPost.getPost().getContentID(), 0, 10)).withRel("sub_reactions"));
            e.add(linkTo(methodOn(ReactionController.class).deleteReaction(sharedPost.getPost().getContentID())).withRel("sub_deleteReaction"));
        }
        return e;

    }
}
