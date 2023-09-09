package com.spring.community.exception;


public class CustomException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public CustomException(ExceptionCode exceptionCode){

        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }



}
