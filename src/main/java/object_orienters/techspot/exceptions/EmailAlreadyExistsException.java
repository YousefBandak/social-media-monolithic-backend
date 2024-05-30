package object_orienters.techspot.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super(email + " Already exists.");
    }
}
