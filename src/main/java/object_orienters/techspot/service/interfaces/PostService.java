package object_orienters.techspot.service.interfaces;

import java.util.Collection;


import object_orienters.techspot.exception.PostNotFoundException;
import object_orienters.techspot.exception.PostUnrelatedToUserException;
import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Post;

public interface PostService {

    public Collection<Post> getTimelinePosts(String username) throws UserNotFoundException;

    public Post addTimelinePosts(String username, Post post) throws UserNotFoundException;

    public Post editTimelinePost(String username, long postId, Post newPost)
            throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException;

    public void deleteTimelinePost(String username, long postId) throws UserNotFoundException, PostNotFoundException;

    public Post getPost(long postId) throws PostNotFoundException;
}
