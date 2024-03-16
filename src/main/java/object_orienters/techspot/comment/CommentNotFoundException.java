package object_orienters.techspot.comment;

public class CommentNotFoundException extends Exception{
    public CommentNotFoundException(long commentId) {
        super("Comment with id " + commentId + " not found");
    }
}
