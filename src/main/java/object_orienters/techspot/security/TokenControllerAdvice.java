package object_orienters.techspot.security;

import object_orienters.techspot.exceptions.TokenRefreshException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class TokenControllerAdvice {
    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleTokenRefreshException(TokenRefreshException exception) {
        Map<String, String> error = Map.of("error", "Token refresh failed", "Timestamp", Instant.now().toString(),
                "message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error);
    }
}
