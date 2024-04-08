package object_orienters.techspot.post;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.UserNotFoundException;

public interface PostService {

        Collection<? extends Content> getPosts(String username) throws UserNotFoundException;

        public Post addTimelinePosts(String username, MultipartFile file, String text, Privacy privacy,
                        List<String> tags) throws UserNotFoundException, IOException;

        public Post editTimelinePost(String username, long postId, MultipartFile file,
                        String text, Privacy privacy)
                        throws UserNotFoundException, PostNotFoundException, PostUnrelatedToUserException, IOException;

        public void deletePost(String username, long postId)
                        throws UserNotFoundException, PostNotFoundException;

        public Post getPost(long postId) throws PostNotFoundException, ContentIsPrivateException;
}
