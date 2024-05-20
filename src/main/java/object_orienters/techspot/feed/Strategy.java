package object_orienters.techspot.feed;

import object_orienters.techspot.comment.CommentRepository;
import object_orienters.techspot.content.ContentRepository;
import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.reaction.ReactionRepository;
import object_orienters.techspot.tag.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public abstract  class Strategy<P, T> {

    @Autowired
    PostRepository postRepository = null;

    @Autowired
    ReactableContentRepository reactableContentRepository = null;

    @Autowired
    CommentRepository commentRepository = null;

    @Autowired
    ReactionRepository reactionRepository = null;

    @Autowired
    ProfileRepository profileRepository = null;

    @Autowired
    ContentRepository contentRepository = null;

    @Autowired
    TagRepository tagRepository = null;


    abstract Page<P> operate(T factor, int pageNumber, int pageSize);

}
