package object_orienters.techspot.feed;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedByTag extends Strategy<Post, String> {
    @Autowired
    public FeedByTag(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> operate(String  tag, int pageNumber, int pageSize) {
        return postRepository.findByTagNameAndPrivacy(tag, List.of(Privacy.PUBLIC), PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending()));
    }

}