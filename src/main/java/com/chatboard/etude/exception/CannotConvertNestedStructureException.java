package com.chatboard.etude.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CannotConvertNestedStructureException extends RuntimeException{

    public CannotConvertNestedStructureException(String message) {
        super(message);
    }
}
