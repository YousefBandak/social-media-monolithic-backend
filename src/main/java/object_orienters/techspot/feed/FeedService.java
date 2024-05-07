package object_orienters.techspot.feed;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.profile.ProfileNotFoundException;
import object_orienters.techspot.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedService {

    @Autowired
    private ProfileRepository profileRepository;

    HashMap<String, Strategy> strategyMap = new HashMap<>();

    public Map<String, Object> feedContent(FeedType feedType, String value, int offset, int limit, String ClientUsername) {
        Strategy strategyReference;
        List<Content> currentFeedReference;
        if (strategyMap.containsKey(feedType.name().concat(value))) {
            strategyReference = strategyMap.get(feedType.name().concat(value));
        } else {
            Strategy strategy = null;
            switch (feedType) {

                case ALL_USERS:
                    strategy = new addByFollowingStrategy(
                            profileRepository.findByUsername(ClientUsername)
                                    .orElseThrow((() -> new ProfileNotFoundException(ClientUsername))));
                    break;
                case TOPIC:
                    strategy = new SearchByTag(value);
                    break;

            }
            strategyReference = strategy;
            strategyMap.put(feedType.name().concat(value), strategy);
        }
        strategyReference.operate();
        currentFeedReference = strategyReference.getContentList();
        currentFeedReference.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        Map<String, Object> map = new HashMap<>();
        map.put("total", currentFeedReference.size());
        map.put("offset", offset);
        map.put("limit", limit);
        map.put("data", currentFeedReference.subList(offset, Math.min(offset + limit, currentFeedReference.size())));
        return map;

    }

    enum FeedType {
        ALL_USERS,
        ONE_USER,
        TOPIC
    }
}
