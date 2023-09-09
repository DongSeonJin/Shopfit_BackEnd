package com.spring.community.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public enum ExceptionCode {
    POST_NOT_FOUND(BAD_REQUEST, "ID_001", "해당되는 id의 게시물을 찾을 수 없습니다."),
    REPLY_NOT_FOUND(BAD_REQUEST, "ID_002", "해당되는 id의 댓글을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
