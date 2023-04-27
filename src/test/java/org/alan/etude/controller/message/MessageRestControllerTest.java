package org.alan.etude.controller.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alan.etude.dto.message.MessageCreateRequestDto;
import org.alan.etude.dto.message.MessageReadConditionDto;
import org.alan.etude.service.message.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.alan.etude.factory.dto.MessageCreateRequestFactory.createMessageCreateRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MessageRestControllerTest {

    @InjectMocks
    MessageRestController messageRestController;
    @Mock
    MessageService messageService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    Long lastMessageId;
    Integer size;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageRestController).build();
    }

    @Test
    void readAllBySenderAndReadConditionTest() throws Exception {
        // given
        lastMessageId = 1L;
        size = 2;

        // when, then
        mockMvc.perform(
                get("/api/messages/sender")
                        .param("lastMessageId", String.valueOf(lastMessageId))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(messageService).readAllMessageBySender(any(MessageReadConditionDto.class));
    }

    @Test
    void readAllByReceiverAndReadConditionTest() throws Exception {
        // given
        lastMessageId = 1L;
        size = 2;

        // when, then
        mockMvc.perform(
                get("/api/messages/receiver")
                        .param("lastMessageId", String.valueOf(lastMessageId))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(messageService).readAllMessageByReceiver(any(MessageReadConditionDto.class));
    }

    @Test
    void readTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id))
                .andExpect(status().isOk());

        verify(messageService).readAMessage(id);
    }

    @Test
    void createTest() throws Exception {
        // given
        MessageCreateRequestDto req = createMessageCreateRequest("content", null, 2L);

        // when, then
        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(messageService).createMessage(req);
    }

    @Test
    void deleteBySenderTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id))
                .andExpect(status().isOk());

        verify(messageService).deleteMessageBySender(id);
    }

    @Test
    void deleteByReceiverTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/messages/receiver/{id}", id))
                .andExpect(status().isOk());

        verify(messageService).deleteMessageByReceiver(id);
    }

}
