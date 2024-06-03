package object_orienters.techspot.feed;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchByCode extends Strategy<Post, List<String>> {

    @Autowired
    public SearchByCode(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    Page<Post> operate(List<String> factor, int pageNumber, int pageSize) {
        System.out.println(factor);
        if (factor.size() == 0 || factor.get(0).equals("")) {
            System.out.println("Empty");
            return postRepository.findAllByCodeAndPrivacy("", Privacy.PUBLIC, PageRequest.of(pageNumber, pageSize));
        }
        else
            return postRepository.findAllByCodeLanguageAndPrivacy(factor, PageRequest.of(pageNumber, pageSize));
    }
}
