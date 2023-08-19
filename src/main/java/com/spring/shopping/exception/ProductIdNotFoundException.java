package com.spring.shopping.exception;

public class ProductIdNotFoundException extends RuntimeException{

    public ProductIdNotFoundException(String message){
        super(message);
    }
}
