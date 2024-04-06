package object_orienters.techspot.search;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import object_orienters.techspot.profile.Profile;

@Getter
public abstract class SearchStrategy {

    private List<Profile> usersList;

    SearchStrategy() {
        usersList = new ArrayList<>();
    }

    public abstract void operate();
}
