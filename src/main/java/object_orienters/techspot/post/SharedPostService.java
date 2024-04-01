package object_orienters.techspot.post;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.UserNotFoundException;


public interface SharedPostService {
    public SharedPost getSharedPost(long sharedPostId) throws UserNotFoundException, PostNotFoundException, ContentIsPrivateException;

    public SharedPost createSharedPost(String username, Long postID, String privacy) throws UserNotFoundException, PostNotFoundException;

    public SharedPost updateSharedPost(long sharedPostId, Privacy newPrivacy) throws PostNotFoundException, ContentIsPrivateException;

    public void deleteSharedPost(String username, long sharedPostId) throws UserNotFoundException, PostNotFoundException, ContentIsPrivateException;


}
