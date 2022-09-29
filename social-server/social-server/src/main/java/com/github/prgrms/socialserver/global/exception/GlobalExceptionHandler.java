package com.github.prgrms.socialserver.global.exception;

import static java.util.Objects.requireNonNull;

import com.github.prgrms.socialserver.global.exception.user.UserException;
import com.github.prgrms.socialserver.global.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> methodArgumentNotValidException(
        MethodArgumentNotValidException e
    ) {
        String errorCode = requireNonNull(e.getFieldError()).getDefaultMessage();
        Response response = Response.fail(errorCode);

        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorCode, "@Valid");
        log.warn("StackTrace : {}", e.getStackTrace());
        log.warn("response={}", response);

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST.value())
            .body(response);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Response> applicationException(UserException e) {
        log.warn(
            LOG_FORMAT,
            e.getClass().getSimpleName(),
            e.getErrorCode(),
            e.getMessage()
        );
        log.warn("StackTrace : {}", e.getStackTrace());
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(Response.fail(e.getMessage()));
    }
}
