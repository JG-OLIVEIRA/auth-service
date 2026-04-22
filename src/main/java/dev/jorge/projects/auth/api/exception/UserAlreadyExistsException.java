package dev.jorge.projects.auth.api.exception;

import dev.jorge.projects.auth.api.common.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.USER_ALREADY_EXISTS_MESSAGE;

    public UserAlreadyExistsException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}