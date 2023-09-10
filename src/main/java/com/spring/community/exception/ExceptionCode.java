package com.spring.community.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public enum ExceptionCode { // 예외 발생시, body에 실어 날려줄 상태, code, message 커스텀

    // 예외 이름들 상수로 구분해서 적어줄 것.
    POST_NOT_FOUND(200, "POST_001", "해당되는 id의 게시물을 찾을 수 없습니다."),
    REPLY_NOT_FOUND(200, "REPLY_001", "해당되는 id의 댓글을 찾을 수 없습니다.");

    // 1. status = 날려줄 상태코드(int형으로 변경 후 400으로 날려줘도 됨)
    // 2. code = 해당 오류가 어느부분과 관련있는지 카테고리화 해주는 코드. 예외 원인 식별하기 편하기에 추가
    // 3. message = 발생한 예외에 대한 설명.

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
