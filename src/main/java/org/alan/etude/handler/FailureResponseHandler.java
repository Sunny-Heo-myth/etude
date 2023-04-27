package org.alan.etude.handler;

import lombok.extern.slf4j.Slf4j;
import org.alan.etude.dto.response.Response;
import org.alan.etude.exception.type.ExceptionType;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
// from ExceptionType & MessageSource
// into exception code & exception message for Response object.
public class FailureResponseHandler {

    private final MessageSource messageSource;

    public FailureResponseHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Response getFailureResponse(ExceptionType exceptionType) {
        log.info("code = {}, msg = {}", getCode(exceptionType.getCode()), getMessage(exceptionType.getMessage()));
        return Response.failure(
                getCode(exceptionType.getCode()),
                getMessage(exceptionType.getMessage())
        );
    }

    public Response getFailureResponse(ExceptionType exceptionType, Object... args) {

        return Response.failure(
                getCode(exceptionType.getCode()),
                getMessage(exceptionType.getMessage(), args)
        );
    }

    private Integer getCode(String key) {
        return Integer.valueOf(messageSource.getMessage(key, null, null));
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key,null, LocaleContextHolder.getLocale());
    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

}
