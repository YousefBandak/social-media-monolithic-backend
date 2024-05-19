package object_orienters.techspot.exceptions;

public class ContentNotFoundException extends RuntimeException {
    public ContentNotFoundException(Long contentId) {
        super("Content not found with ID: " + contentId);
    }
}
