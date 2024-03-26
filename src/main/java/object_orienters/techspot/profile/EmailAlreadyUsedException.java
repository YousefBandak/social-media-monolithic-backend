package object_orienters.techspot.profile;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super(email + " Already Used.");
    }
}
