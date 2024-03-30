package object_orienters.techspot.post;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.UserNotFoundException;


public interface SharedPostService {
    public SharedPost getSharedPost(long sharedPostId) throws UserNotFoundException, PostNotFoundException;

    public SharedPost addSharedPost(String username, Post post, Privacy privacy) throws UserNotFoundException, PostNotFoundException;

    public SharedPost editSharedPost(String username, long sharedPostId,Privacy newPrivacy)
            throws UserNotFoundException, PostNotFoundException;

    public void deleteSharedPost(String username, long sharedPostId) throws UserNotFoundException, PostNotFoundException;


}
