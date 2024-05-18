package object_orienters.techspot.feed;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.profile.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedByFollowingStrategy extends Strategy<Post, Profile> {
    @Autowired
    public FeedByFollowingStrategy(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> operate(Profile profile, int pageNumber, int pageSize) {
        return postRepository.findPostsByContentAuthorsAndPrivacy(profile.getFollowing(),
                List.of(Privacy.PUBLIC, Privacy.FRIENDS), PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending()));
    }
}