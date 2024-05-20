package object_orienters.techspot.exceptions;

public class UsernameAlreadyUsedExeption extends RuntimeException {
    public UsernameAlreadyUsedExeption(String username) {
        super(username + " Already Used.");
    }
}
