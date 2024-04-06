package object_orienters.techspot.search;

import org.springframework.beans.factory.annotation.Autowired;

import object_orienters.techspot.profile.ProfileRepository;

public class SearchByUsername extends SearchStrategy {

    private String username;
    @Autowired
    private ProfileRepository profileRepository;

    SearchByUsername(String username) {
        this.username = username;

    }

    @Override
    public void operate() {
        this.getUsersList().add(profileRepository.findByUsername(username).get());
    }
}
