package com.github.prgrms.socialserver.controller.dto;

public enum UserResult {

    JOIN_SUCCESS("가입완료");
    private String message;

    UserResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
