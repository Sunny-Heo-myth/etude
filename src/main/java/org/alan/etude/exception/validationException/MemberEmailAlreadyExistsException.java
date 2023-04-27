package org.alan.etude.exception.validationException;

public class MemberEmailAlreadyExistsException extends RuntimeException{
    public MemberEmailAlreadyExistsException(String message) {
        super(message);
    }
}
