package object_orienters.techspot.feed;

import org.springframework.beans.factory.annotation.Autowired;

import object_orienters.techspot.post.PostRepository;

public class SearchByTag extends Strategy {
    @Autowired
    private PostRepository postRepository;
    private String tag;

    public SearchByTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void operate() {
        this.getContentList().addAll(postRepository.findByTagName(tag));
    }

}
