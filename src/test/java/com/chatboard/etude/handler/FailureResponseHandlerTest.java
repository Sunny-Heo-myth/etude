package com.chatboard.etude.handler;

import com.chatboard.etude.dto.response.Failure;
import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.exception.type.ExceptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FailureResponseHandlerTest {

    FailureResponseHandler failureResponseHandler;

    @BeforeEach
    void beforeEach() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/exception");
        failureResponseHandler = new FailureResponseHandler(messageSource);
    }

    @Test
    void getFailureResponseNoArgsTest() {
        // given, when
        Response failureResponse = failureResponseHandler.getFailureResponse(ExceptionType.EXCEPTION);

        // then
        assertThat(failureResponse.getCode()).isEqualTo(-1000);
        assertThat(((Failure) failureResponse.getResponseResult()).getMessage()).isEqualTo("exception");
    }

    @Test
    void getFailureResponseWithArgsTest() {
        // given, when
        Response failureResponse = failureResponseHandler.getFailureResponse(ExceptionType.BIND_EXCEPTION, "my args");

        // then
        assertThat(failureResponse.getCode()).isEqualTo(-1003);
        assertThat(((Failure) failureResponse.getResponseResult()).getMessage()).isEqualTo("my args");
    }
}
