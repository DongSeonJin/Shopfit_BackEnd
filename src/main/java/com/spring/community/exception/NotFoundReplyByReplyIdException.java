package com.spring.community.exception;

public class NotFoundReplyByReplyIdException extends RuntimeException {
    public NotFoundReplyByReplyIdException (String message) {
        super(message);
    }
}
