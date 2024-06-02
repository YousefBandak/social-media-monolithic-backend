package object_orienters.techspot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExceptionsResponse {
    public static ResponseEntity<?> getErrorResponseEntity(Exception e, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("error", e.getMessage());
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
