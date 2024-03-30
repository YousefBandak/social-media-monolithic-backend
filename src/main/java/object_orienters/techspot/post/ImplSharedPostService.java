package object_orienters.techspot.post;

import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;

public class ImplSharedPostService implements SharedPostService{
    private final ProfileRepository profileRepository;
    private final SharedPost sharedPostRepository;

    public ImplSharedPostService(ProfileRepository profileRepository, SharedPost sharedPostRepository) {
        this.profileRepository = profileRepository;
        this.sharedPostRepository = sharedPostRepository;
    }

    @Override
    public SharedPost getSharedPost(String username) throws UserNotFoundException, PostNotFoundException {
//        Profile user = profileRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
//        SharedPost sharedPost = new SharedPost(user, post, privacy);
//        sharedPostRepository.save(sharedPost);
//        user.getSharedPosts().add(sharedPost);
//        profileRepository.save(user);
        //return sharedPost;
        return null;

    }

    @Override
    public SharedPost addSharedPost(String username) throws UserNotFoundException, PostNotFoundException {
        return null;
    }

    @Override
    public SharedPost editSharedPost(String username, long sharedPostId) throws UserNotFoundException, PostNotFoundException {
        return null;
    }

    @Override
    public void deleteSharedPost(String username, long postId) throws UserNotFoundException, PostNotFoundException {

    }
}
