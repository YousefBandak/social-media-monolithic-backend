package object_orienters.techspot.search;

import org.springframework.beans.factory.annotation.Autowired;

import object_orienters.techspot.profile.ProfileRepository;

public class SearchByName extends SearchStrategy {

    private String name;
    @Autowired
    private ProfileRepository profileRepository;

    SearchByName(String name) {
        this.name = name;

    }

    @Override
    public void operate() {
        this.getUsersList().addAll(profileRepository.findByName(name));
    
    }

}
