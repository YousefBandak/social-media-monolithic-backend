package object_orienters.techspot.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super(email + " Already Used.");
    }
}
