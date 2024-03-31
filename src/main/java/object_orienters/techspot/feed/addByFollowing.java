package object_orienters.techspot.feed;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.profile.Profile;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class addByFollowing implements Strategy{

    Profile profile;

    public addByFollowing(Profile profile){
        this.profile = profile;
    }
    @Override
    public void operate(List<? extends Content> contentList) {
        profile.getFollowing().forEach(following -> {
            contentList.addAll((Collection)following.getTimelinePosts().stream().filter(content -> ((Content) content).getTimestamp().after(profile.getLastLogin())).collect(Collectors.toList()));
        });
    }
}
