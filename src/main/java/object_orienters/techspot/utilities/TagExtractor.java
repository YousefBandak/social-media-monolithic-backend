package object_orienters.techspot.utilities;

import object_orienters.techspot.post.Post;
import object_orienters.techspot.tag.Tag;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagExtractor {
    private static final Pattern TAG_PATTERN = Pattern.compile("#\\w+");

    public static Set<Tag> extractTags(String text, Post post, Function<String, Tag> tagCreator) {
        Matcher matcher = TAG_PATTERN.matcher(text);
        Set<Tag> tags = new HashSet<>();
        while (matcher.find()) {
            try {
                String tagName = matcher.group().substring(1).toLowerCase().trim(); // Remove the '#' prefix
                Tag tag = tagCreator.apply(tagName);
                if (tag != null) {
                    updateTagWithPostId(tag, post);
                    tags.add(tag);
                } else {
                    // Handle the case where tag creation fails
                    System.err.println("Failed to create or retrieve tag: " + tagName);
                }
            } catch (Exception e) {
                System.err.println("Error processing tag: " + e.getMessage());
            }
        }
        return tags;
    }

    private static void updateTagWithPostId(Tag tag, Post post) {
        StringBuilder sb = new StringBuilder();
        if (tag.getPosts() != null && !tag.getPosts().isEmpty()) {
            sb.append(tag.getPosts()).append(",");
        }
        sb.append(post.getContentID());
        tag.setPosts(sb.toString());
    }
}
