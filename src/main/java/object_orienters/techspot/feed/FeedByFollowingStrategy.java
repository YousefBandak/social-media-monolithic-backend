package object_orienters.techspot.feed;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.content.ContentRepository;
import object_orienters.techspot.model.ContentType;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedByFollowingStrategy extends Strategy<Content, Profile> {
    @Autowired
    public FeedByFollowingStrategy(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public Page<Content> operate(Profile profile, int pageNumber, int pageSize) {
        return contentRepository.findAllByMainAuthorsAndContentTypeAndPrivacy(profile.getFollowing(),
                List.of(Privacy.PUBLIC, Privacy.FRIENDS), List.of(ContentType.Post, ContentType.SharedPost), PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending()));
    }
}