package com.spring.community.exception;

public class PostIdNotFoundException extends RuntimeException {
    public PostIdNotFoundException(String message) {
        super(message);
    }
}
