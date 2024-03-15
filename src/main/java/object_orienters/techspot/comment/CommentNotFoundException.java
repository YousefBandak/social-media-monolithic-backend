package object_orienters.techspot.comment;

public class CommentNotFoundException extends Exception{
    public CommentNotFoundException(String commentId) {
        super("Comment with id " + commentId + " not found");
    }
}
