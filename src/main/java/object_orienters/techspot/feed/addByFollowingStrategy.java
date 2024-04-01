package object_orienters.techspot.feed;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;


public class addByFollowingStrategy extends Strategy {

    Profile profile;


    public addByFollowingStrategy(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void operate() {
        profile.getFollowing().forEach(following -> this.getContentList().addAll(following.getTimelinePostsByPrivacy(Privacy.PUBLIC).stream().filter(content -> content.getTimestamp().after(profile.getLastLogin())).toList()));
    }
}
