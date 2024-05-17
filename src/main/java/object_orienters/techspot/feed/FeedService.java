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

    @Autowired
    public FeedService(AddByFollowingStrategy addByFollowingStrategy, ProfileRepository profileRepository) {
        this.addByFollowingStrategy = addByFollowingStrategy;
        this.profileRepository = profileRepository;
    }

    public Page<Post> feedContent(FeedType feedType, String value, int pageNumber, int pageSize, String clientUsername) {
        Profile profile = profileRepository.findByUsername(clientUsername).orElseThrow(() -> new ProfileNotFoundException(clientUsername));
        System.out.println("Profile: " + profile);
        Strategy strategyReference = null;
        switch (feedType) {

            case ALL_USERS:
                System.out.println("ALL_USERS");
                strategyReference = addByFollowingStrategy;
                break;
            case TOPIC:
                //  strategy = new SearchByTag(value);
                break;

        }

//        System.out.println("Strategy: " + strategyReference);
//        System.out.println("count: " + strategyReference.getPostCount(profile));
//        Map<String, Object> map = new HashMap<>();
//        map.put("total", strategyReference.getPostCount(profile));
//        map.put("offset", offset);
//        map.put("limit", limit);
//        map.put("data", strategyReference.operate(profile, limit, offset));
//        System.out.println("Map: " + map);
//        return map;

        return strategyReference.operate(profile, pageNumber, pageSize);

    }

    enum FeedType {
        ALL_USERS, ONE_USER, TOPIC
    }
}
