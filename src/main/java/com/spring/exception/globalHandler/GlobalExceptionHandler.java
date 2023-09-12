package com.spring.exception.globalHandler;

import com.spring.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// 모든 예외처리를 한곳에서 처리해주는 곳.
@RestControllerAdvice // 비동기식 controller의 예외처리 관리시 붙이는 어노테이션
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class) // 만들어준 커스텀익셉션 발생시 처리해주는 곳
    public ResponseEntity<ErrorResponse> handleIdNotFoundException(CustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getExceptionCode().getStatus(),
                ex.getExceptionCode().getCode(),
                ex.getExceptionCode().getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));

    }


}
