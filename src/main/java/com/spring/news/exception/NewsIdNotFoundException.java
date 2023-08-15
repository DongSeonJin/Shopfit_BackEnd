package com.spring.news.exception;

public class NewsIdNotFoundException extends RuntimeException{
    // RuntimeException을 상속받아 언체크드 익셉션으로 만들기

    public NewsIdNotFoundException(String message) {
        super(message);
    }
}
