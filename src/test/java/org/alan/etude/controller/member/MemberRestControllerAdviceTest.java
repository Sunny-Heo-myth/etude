package org.alan.etude.controller.member;

import org.alan.etude.advice.ExceptionAdvice;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.handler.FailureResponseHandler;
import org.alan.etude.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberRestControllerAdviceTest {

    @InjectMocks
    MemberRestController memberRestController;
    @Mock
    MemberService memberService;
    @Mock
    FailureResponseHandler failureResponseHandler;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasenames("i18n/exception");

        mockMvc = MockMvcBuilders.standaloneSetup(memberRestController)
                .setControllerAdvice(new ExceptionAdvice(failureResponseHandler))
                .build();
    }

    @Test
    void readMemberNotFoundExceptionTest() throws Exception {
        // given
        given(memberService.readMember(anyLong())).willThrow(MemberNotFoundException.class);

        // when, then
        mockMvc.perform(
                get("/api/members/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMemberNotFoundExceptionTest() throws Exception {
        // given
        doThrow(MemberNotFoundException.class).when(memberService).deleteMember(anyLong());

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
