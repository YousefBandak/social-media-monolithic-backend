package object_orienters.techspot.feed;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SearchByCode extends Strategy<Post, String> {

    @Autowired
    public SearchByCode(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    Page<Post> operate(String factor, int pageNumber, int pageSize) {
        return postRepository.findAllByCodeAndPrivacy(factor, Privacy.PUBLIC, PageRequest.of(pageNumber, pageSize));
    }
}
