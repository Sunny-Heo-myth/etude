package com.chatboard.etude.exception;

public class MemberNickNameAlreadyExistsException extends RuntimeException{
    public MemberNickNameAlreadyExistsException(String message) {
        super(message);
    }
}
