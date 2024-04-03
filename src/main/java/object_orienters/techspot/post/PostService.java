package object_orienters.techspot.post;

import java.io.IOException;
import java.util.Collection;

import org.springframework.web.multipart.MultipartFile;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.UserNotFoundException;

public interface PostService {

    Collection<? extends Content> getTimelinePosts(String username) throws UserNotFoundException;

    public Post addTimelinePosts(String username, MultipartFile file,
            String text,
            String name,
            String type, Privacy privacy) throws UserNotFoundException, IOException;

    //public SharedPost addSharedPost(String username, Post post, Privacy privacy) throws UserNotFoundException;

    public Post editTimelinePost(String username, long postId, Post newPost)
            throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException;

    public void deleteTimelinePost(String username, long postId) throws UserNotFoundException, PostNotFoundException;

    public Post getPost(long postId) throws PostNotFoundException, ContentIsPrivateException;
}
