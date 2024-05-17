//package object_orienters.techspot.feed;
//
//import object_orienters.techspot.post.Post;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import object_orienters.techspot.post.PostRepository;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.List;
//
//
//public class SearchByTag implements Strategy {
//
//
//    private PostRepository postRepository;
//
//    @Autowired
//    public SearchByTag(PostRepository postRepository) {
//        this.postRepository = postRepository;
//    }
//    private String tag;
//
//    public SearchByTag(String tag) {
//        this.tag = tag;
//    }
//
//    @Override
//    public List<Post> operate(int offset, int limit) {
//        return postRepository.findByTag(tag, PageRequest.of(offset, limit));
//    }
//
//    @Override
//    public Long getPostCount() {
//        return 0L;
//
//    }
//
//}
