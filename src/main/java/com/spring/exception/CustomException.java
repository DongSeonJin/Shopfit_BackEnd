package com.spring.exception;


public class CustomException extends RuntimeException{
    // runtimeException 발생시 날려줄 메시지들을 enum에 작성해놓고,
    // CustomException(현재 클래스) 에서 enum 객체를 생성
    private final ExceptionCode exceptionCode;

    public CustomException(ExceptionCode exceptionCode){

        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }





}
