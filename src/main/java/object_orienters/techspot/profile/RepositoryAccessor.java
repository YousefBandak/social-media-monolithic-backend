package object_orienters.techspot.profile;

import org.springframework.stereotype.Component;



@Component
public class RepositoryAccessor {

    private static ProfileRepository profileRepository;

    public RepositoryAccessor(ProfileRepository repo) {
        RepositoryAccessor.profileRepository = repo;
    }

    public static ProfileRepository getProfileRepository() {
        return profileRepository;
    }
}


