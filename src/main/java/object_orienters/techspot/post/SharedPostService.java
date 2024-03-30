package object_orienters.techspot.post;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.UserNotFoundException;

import java.util.Collection;

public interface SharedPostService {
    public SharedPost getSharedPost(String username) throws UserNotFoundException, PostNotFoundException;

    public SharedPost addSharedPost(String username) throws UserNotFoundException, PostNotFoundException;

    public SharedPost editSharedPost(String username, long sharedPostId)
            throws UserNotFoundException, PostNotFoundException;

    public void deleteSharedPost(String username, long postId) throws UserNotFoundException, PostNotFoundException;


}
