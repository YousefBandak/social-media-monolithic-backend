package object_orienters.techspot.post;

import java.util.Collection;


import object_orienters.techspot.profile.UserNotFoundException;

public interface PostService {

    public Collection<Post> getTimelinePosts(String username) throws UserNotFoundException;

    public Post addTimelinePosts(String username, Post post) throws UserNotFoundException;

    public Post editTimelinePost(String username, long postId, Post newPost)
            throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException;

    public void deleteTimelinePost(String username, long postId) throws UserNotFoundException, PostNotFoundException;

    public Post getPost(long postId) throws PostNotFoundException;
}
