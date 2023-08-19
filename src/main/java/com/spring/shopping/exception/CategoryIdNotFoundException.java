package com.spring.shopping.exception;

public class CategoryIdNotFoundException extends RuntimeException{

    public CategoryIdNotFoundException(String message) {
        super(message);
    }
}
