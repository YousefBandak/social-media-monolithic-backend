package object_orienters.techspot.post;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

public interface PostService {

        Collection<? extends Content> getPosts(String username) throws UserNotFoundException;

        public Post addPosts(String username, MultipartFile file, String text, Privacy privacy)
                        throws UserNotFoundException, IOException;

        // public SharedPost addSharedPost(String username, Post post, Privacy privacy)
        // throws UserNotFoundException;

        public Post editPost(String username, long postId, Post newPost)
                        throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException;

        public void deletePost(String username, long postId)
                        throws UserNotFoundException, PostNotFoundException;

        public Post getPost(long postId) throws PostNotFoundException, ContentIsPrivateException;
}
