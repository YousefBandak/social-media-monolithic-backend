package object_orienters.techspot.security;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostNotFoundException;
import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.security.model.User;
import object_orienters.techspot.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.runtime.SwitchBootstraps;

@Service
public class PermissionService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PostRepository postRepository;

    public boolean canAccessPost(Long postId, String username) throws PostNotFoundException {
        Post post = postRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        switch (post.getPrivacy()) {
            case PUBLIC:
                return true;
            case PRIVATE:
                return post.getContentAuthor().getUsername().equals(username);
            case FRIENDS:
                return post.getContentAuthor().getUsername().equals(username) || profile.getFollowing().contains(post.getContentAuthor());
            default:
                return false;
        }


    }

    public boolean isPostPublic(Long postId) throws PostNotFoundException {
        Post post = postRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return post.getPrivacy().equals(Privacy.PUBLIC);
    }
}
