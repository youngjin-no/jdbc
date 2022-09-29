package com.github.prgrms.socialserver.global.exception.user;

import org.springframework.http.HttpStatus;


public enum UserErrorCode {
    ENTITY_NOT_FOUND("ENTITY", "데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("PASSWORD", "비밀번호가 다릅니다.", HttpStatus.UNAUTHORIZED),
    INVALID_EMAIL("EMAIL", "존재하는 이메일 입니다.", HttpStatus.BAD_REQUEST);

    private String code;
    private String message;
    private HttpStatus httpStatus;

    UserErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

