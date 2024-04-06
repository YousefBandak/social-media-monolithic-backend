package object_orienters.techspot.feed;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.profile.ProfileNotFoundException;
import object_orienters.techspot.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FeedService {

    @Autowired
    private ProfileRepository profileRepository;

    HashMap<String, Strategy> strategyMap = new HashMap<>();

    public List<Content> feedContent(FeedType feedType, String value, int offset, int limit, String ClientUsername) {
        List<Content> CurrentFeedReference = new ArrayList<>();
        if (strategyMap.containsKey(feedType.name().concat(value))) {
            CurrentFeedReference = strategyMap.get(feedType.name().concat(value)).getContentList();
        } else {
            Strategy strategy = null;
            switch (feedType) {

                case ALL_USERS:
                    strategy = new addByFollowingStrategy(
                            profileRepository.findByUsername(ClientUsername).orElseThrow((() -> new ProfileNotFoundException(ClientUsername))));
                    break;
                case TOPIC:
                    strategy = new SearchByTag(value);
                    break;

            }
            strategyMap.put(feedType.name().concat(value), strategy);
            strategy.operate();
            CurrentFeedReference = strategy.getContentList();
            CurrentFeedReference.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        }
        return CurrentFeedReference.subList(offset, Math.min(offset + limit, CurrentFeedReference.size()));

    }

    enum FeedType {
        ALL_USERS,
        ONE_USER,
        TOPIC
    }
}
