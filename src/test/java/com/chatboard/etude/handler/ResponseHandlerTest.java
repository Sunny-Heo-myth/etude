package com.chatboard.etude.handler;

import com.chatboard.etude.dto.response.Failure;
import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.exception.type.ExceptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseHandlerTest {

    ResponseHandler responseHandler;

    @BeforeEach
    void beforeEach() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/exception");
        responseHandler = new ResponseHandler(messageSource);
    }

    @Test
    void getFailureResponseNoArgsTest() {
        // given, when
        Response failureResponse = responseHandler.getFailureResponse(ExceptionType.EXCEPTION);

        // then
        assertThat(failureResponse.getCode()).isEqualTo(-1000);
        assertThat(((Failure) failureResponse.getResult()).getMessage()).isEqualTo("오류가 발생하였습니다.");
    }

    @Test
    void getFailureResponseWithArgsTest() {
        // given, when
        Response failureResponse = responseHandler.getFailureResponse(ExceptionType.BIND_EXCEPTION, "my args");

        // then
        assertThat(failureResponse.getCode()).isEqualTo(-1003);
        assertThat(((Failure) failureResponse.getResult()).getMessage()).isEqualTo("my args");
    }
}
