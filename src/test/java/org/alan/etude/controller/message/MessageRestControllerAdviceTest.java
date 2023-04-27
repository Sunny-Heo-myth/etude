package org.alan.etude.controller.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alan.etude.advice.ExceptionAdvice;
import org.alan.etude.dto.message.MessageCreateRequestDto;
import org.alan.etude.exception.notFoundException.MessageNotFoundException;
import org.alan.etude.handler.FailureResponseHandler;
import org.alan.etude.service.message.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.alan.etude.factory.dto.MessageCreateRequestFactory.createMessageCreateRequest;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MessageRestControllerAdviceTest {

    @InjectMocks
    MessageRestController messageRestController;
    @Mock
    MessageService messageService;
    @Mock
    FailureResponseHandler failureResponseHandler;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasenames("i18n/exception");

        mockMvc = MockMvcBuilders.standaloneSetup(messageRestController)
                .setControllerAdvice(new ExceptionAdvice(failureResponseHandler))
                .build();
    }

    @Test
    void readTest() throws Exception {
        // given
        Long id = 1L;
        given(messageService.readAMessage(id)).willThrow(MessageNotFoundException.class);

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTest() throws Exception {
        // given
        MessageCreateRequestDto request = createMessageCreateRequest("content", null, 2L);
        doThrow(MessageNotFoundException.class).when(messageService).createMessage(request);

        // when, then
        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBySenderTest() throws Exception {
        // given
        Long id = 1L;
        doThrow(MessageNotFoundException.class).when(messageService).deleteMessageBySender(id);

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteByReceiverTest() throws Exception {
        // given
        Long id = 1L;
        doThrow(MessageNotFoundException.class).when(messageService).deleteMessageByReceiver(id);

        // when, then
        mockMvc.perform(
                delete("/api/messages/receiver/{id}", id))
                .andExpect(status().isNotFound());
    }

}
