package object_orienters.techspot.feed;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostRepository;
import object_orienters.techspot.profile.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddByFollowingStrategy extends Strategy<Profile> {
    @Autowired
    public AddByFollowingStrategy(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> operate(Profile profile, int pageNumber, int pageSize) {
        List<Privacy> privacies = new ArrayList<>();
        privacies.add(Privacy.PUBLIC);

        Page e = postRepository.findPostsByContentAuthorsAndPrivacy(profile.getFollowing(), privacies, PageRequest.of(pageNumber,pageSize , Sort.by("timestamp").descending()));
        System.out.println("Posts: " + e);
        return e;
    }



    @Override
    public Long getPostCount(Profile profile){
       //List<Profile> authors = List.copyOf();
      //  authors.add(profile);
        List<Privacy> privacies = new ArrayList<>();
        privacies.add(Privacy.PUBLIC);
        return postRepository.countPostsByContentAuthorsAndPrivacy(profile.getFollowing(), privacies);
    }
}