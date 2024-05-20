package object_orienters.techspot.utilities;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.content.ContentRepository;
import object_orienters.techspot.content.ReactableContent;
import object_orienters.techspot.exceptions.PostNotFoundException;
import object_orienters.techspot.exceptions.UserNotFoundException;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ContentRepository contentRepository;

    public boolean canAccessPost(Long postId, String username) throws PostNotFoundException {
        Content post = contentRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        switch (post.getPrivacy()) {
            case PUBLIC:
                return true;
            case PRIVATE:
                Profile mainAuthor;
                if (post instanceof ReactableContent)
                    mainAuthor = post.getContentAuthor();
                else
                    mainAuthor = post.getMainAuthor();
                return mainAuthor.getUsername().equals(username);
            case FRIENDS:
                Profile contentAuthor;
                if (post instanceof ReactableContent)
                    contentAuthor = post.getContentAuthor();
                else
                    contentAuthor = post.getMainAuthor();
                return contentAuthor.getUsername().equals(username) ||
                        profile.getFollowing().contains(contentAuthor);
            default:
                return false;
        }


    }

    public boolean isPostPublic(Long postId) throws PostNotFoundException {
        Content post = contentRepository.findByContentID(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return post.getPrivacy().equals(Privacy.PUBLIC);
    }
}
