package object_orienters.techspot.feed;

import object_orienters.techspot.post.Post;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileNotFoundException;
import object_orienters.techspot.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FeedService {


    private ProfileRepository profileRepository;
    private AddByFollowingStrategy addByFollowingStrategy;
    private SearchByTag searchByTag;

    @Autowired
    public FeedService(AddByFollowingStrategy addByFollowingStrategy, ProfileRepository profileRepository, SearchByTag searchByTag) {
        this.addByFollowingStrategy = addByFollowingStrategy;
        this.profileRepository = profileRepository;
        this.searchByTag = searchByTag;
    }

    public Page<Post> feedContent(FeedType feedType, String value, int pageNumber, int pageSize, String clientUsername) {
        switch (feedType) {

            case ALL_USERS:
                Profile profile = profileRepository.findByUsername(clientUsername).orElseThrow(() -> new ProfileNotFoundException(clientUsername));
                return addByFollowingStrategy.operate(profile, pageNumber, pageSize);
            case ONE_USER:
                return null;
            case TOPIC:
                return searchByTag.operate(value, pageNumber, pageSize);
            default:
                return null;
        }



    }

    enum FeedType {
        ALL_USERS, ONE_USER, TOPIC
    }
}
