package object_orienters.techspot.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import object_orienters.techspot.profile.Profile;

@Service
public class SearchService {
    HashMap<String, SearchStrategy> strategyMap = new HashMap<>();

    public List<Profile> search(String keyword, SearchType type, int offset, int limit) {
        List<Profile> users = new ArrayList<>();
        if (strategyMap.containsKey(type.name().concat(keyword))) {
            users = strategyMap.get(type.name().concat(keyword)).getUsersList();
        } else {
            SearchStrategy strategy = null;
            switch (type) {
                case USERNAME:
                    strategy = new SearchByUsername(keyword);
                    break;
                case NAME:
                    strategy = new SearchByName(keyword);
                    break;
            }
            strategy.operate();
            strategyMap.put(type.name().concat(keyword), strategy);
            users.addAll(strategyMap.get(type.name().concat(keyword)).getUsersList());
        }
        return users.subList(offset, limit);
    }

}

enum SearchType {
    USERNAME, NAME
}