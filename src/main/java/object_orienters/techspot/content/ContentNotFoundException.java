package object_orienters.techspot.content;

import object_orienters.techspot.Content;

public class ContentNotFoundException extends RuntimeException {
    ContentRepository contentRepository;
    public ContentNotFoundException(Long contentId) {
        super("Content not found with ID: " + contentId);
    }
}
