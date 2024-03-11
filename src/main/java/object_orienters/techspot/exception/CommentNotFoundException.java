package object_orienters.techspot.exception;

public class CommentNotFoundException extends Exception{
    public CommentNotFoundException(String commentId) {
        super("Comment with id " + commentId + " not found");
    }
}
