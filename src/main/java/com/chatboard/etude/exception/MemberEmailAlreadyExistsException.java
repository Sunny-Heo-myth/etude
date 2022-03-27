package com.chatboard.etude.exception;

public class MemberEmailAlreadyExistsException extends RuntimeException{
    public MemberEmailAlreadyExistsException(String message) {
        super(message);
    }
}
