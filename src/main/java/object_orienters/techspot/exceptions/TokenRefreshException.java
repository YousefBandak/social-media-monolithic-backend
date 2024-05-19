package object_orienters.techspot.exceptions;

public class TokenRefreshException extends RuntimeException {

    public TokenRefreshException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }

    public TokenRefreshException(String token, String message, Throwable cause) {
        super(String.format("Failed for [%s]: %s", token, message), cause);
    }
}
