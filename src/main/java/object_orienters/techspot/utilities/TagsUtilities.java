package object_orienters.techspot.utilities;

import object_orienters.techspot.post.Post;
import object_orienters.techspot.tag.Tag;
import object_orienters.techspot.tag.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagsUtilities {

    TagRepository tagRepository;

    @Autowired
    public TagsUtilities(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
    public void handleAddTags(String text, Post post) {
        Set<Tag> newTags = TagExtractor.extractTags(text, post, tagName -> {
            return tagRepository.findByTagName(tagName.trim()).orElseGet(() -> {
                Tag newTag = new Tag();
                newTag.setTagName(tagName.trim());
                return newTag;
            });
        });
        String tagsString = newTags.stream().map(Tag::getTagName).collect(Collectors.joining(","));
        post.setTags(tagsString);
        newTags.forEach(tag -> tagRepository.save(tag));
    }

    public void handleDeleteTags(Post post) {
        String postTags = post.getTags();
        if (postTags == null || postTags.isEmpty())
            return;

        String[] tagsArray = postTags.split(",");
        List<String> tagsList = new ArrayList<>(Arrays.asList(tagsArray));


        tagsList.stream().forEach(tagString -> {
            Tag tag = tagRepository.findById(tagString.trim()).orElseThrow(() -> new RuntimeException("Tag not found"));
            String posts = tag.getPosts();
            String[] postsArray = posts.split(",");
            List<String> postList = new ArrayList<>(Arrays.asList(postsArray));
            if (postList.size() == 1) {
                tagRepository.delete(tag);
            } else {
                postList.remove(Long.toString(post.getContentID()));
                tag.setPosts(String.join(",", postList));
                tagRepository.save(tag);
            }
        });
    }
}
