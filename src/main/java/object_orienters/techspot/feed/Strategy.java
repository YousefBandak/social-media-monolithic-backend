package object_orienters.techspot.feed;

import object_orienters.techspot.comment.Comment;
import object_orienters.techspot.comment.CommentRepository;
import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.profile.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.swing.text.AbstractDocument;
import java.util.List;

@Service
public abstract  class Strategy<P, T> {

    @Autowired
    PostRepository postRepository = null;

    @Autowired
    ReactableContentRepository reactableContentRepository = null;

    @Autowired
    CommentRepository commentRepository = null;

    abstract Page<P> operate(T factor, int pageNumber, int pageSize);

}
