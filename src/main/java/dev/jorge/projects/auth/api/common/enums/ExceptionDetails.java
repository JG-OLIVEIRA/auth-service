package dev.jorge.projects.auth.api.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ExceptionDetails {

    USER_ALREADY_EXISTS_MESSAGE("Essa conta já existe", HttpStatus.CONFLICT),
    USER_NOT_FOUND_MESSAGE("Essa conta não existe", HttpStatus.NOT_FOUND);

    @Getter
    private final HttpStatus httpStatus;
    private final String message;

    ExceptionDetails(String message, HttpStatus httpStatus){
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String formatErrorMessage(String value){
        return String.format(this.message, value);
    }
}