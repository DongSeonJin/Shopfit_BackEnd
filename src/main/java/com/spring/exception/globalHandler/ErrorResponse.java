package com.spring.exception.globalHandler;

import lombok.*;

// globalExceptionHandler(전역예외처리)에서 예외 발생 처리시 보내줄 응답 객체(status, code, message)
public class ErrorResponse {

    private int status;
    private String code;
    private String message;

    public ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;

    }


    public int getStatus(){
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
