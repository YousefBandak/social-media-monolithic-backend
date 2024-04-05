package object_orienters.techspot.profile;

public class UserCannotFollowSelfException extends RuntimeException {
    public UserCannotFollowSelfException(String username) {
        super("Cannot follow this profile: " + username);
    }
}
