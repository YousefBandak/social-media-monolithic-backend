package object_orienters.techspot.profile;

public class UsernameAlreadyUsedExeption extends RuntimeException {
    public UsernameAlreadyUsedExeption(String username) {
        super(username + " Already Used.");
    }
}
