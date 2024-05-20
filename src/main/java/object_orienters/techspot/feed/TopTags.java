package object_orienters.techspot.feed;

import object_orienters.techspot.tag.Tag;
import object_orienters.techspot.tag.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TopTags extends Strategy<Tag, String> {

    @Autowired
    public TopTags(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    Page<Tag> operate(String factor, int pageNumber, int pageSize) {
        return tagRepository.findMostPopularTags(PageRequest.of(pageNumber, pageSize));
    }
}
