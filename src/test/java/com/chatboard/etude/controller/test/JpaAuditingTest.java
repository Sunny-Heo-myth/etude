package com.chatboard.etude.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.chatboard.etude.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class JpaAuditingTest {

    @InjectMocks
    TestController testController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
    }

//    @Test
//    void dateTest() throws Exception {
//
//        // when, then
//        mockMvc.perform(
//                        get("/test/format")
//                                .contentType(MediaType.ALL_VALUE)
//                                .content(objectMapper.writeValueAsString())
//                .andExpect(status().isCreated());
//
//        verify(signService).signUp(request);
//    }
}
