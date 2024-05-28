package object_orienters.techspot.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super(username + " Already Exists.");
    }
}
