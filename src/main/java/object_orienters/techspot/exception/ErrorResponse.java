package object_orienters.techspot.exception;

import org.springframework.hateoas.EntityModel;

public class ErrorResponse<T> extends EntityModel<T> {
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
