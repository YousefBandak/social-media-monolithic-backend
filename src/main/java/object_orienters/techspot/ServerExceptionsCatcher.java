package object_orienters.techspot;

import object_orienters.techspot.exceptions.ContentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice(basePackages = {
        // "object_orienters.techspot.message",
        "object_orienters.techspot.comment",
        "object_orienters.techspot.profile",
        "object_orienters.techspot.security",
        "object_orienters.techspot.chat",
        "object_orienters.techspot.content",
        "object_orienters.techspot.feed",
        "object_orienters.techspot.model",
        "object_orienters.techspot.post",
        "object_orienters.techspot.reaction"
})
public class ServerExceptionsCatcher extends ResponseEntityExceptionHandler {
    @SuppressWarnings("rawtypes")
    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity handleAuthenticationException(Exception ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("errors", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ContentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<?> handleContentNotFoundException(ContentNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("errors", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}
