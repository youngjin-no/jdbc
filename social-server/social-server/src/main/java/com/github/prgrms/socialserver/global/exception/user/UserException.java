package com.github.prgrms.socialserver.global.exception.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {
    private final UserErrorCode errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    public UserException(UserErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), errorCode.getHttpStatus());
    }

    public UserException(UserErrorCode errorCode, String customMessage, HttpStatus status) {
        super(customMessage);
        this.errorCode = errorCode;
        this.message = customMessage;
        this.httpStatus = status;
    }

    public UserErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
