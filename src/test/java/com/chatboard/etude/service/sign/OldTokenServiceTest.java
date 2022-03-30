package com.chatboard.etude.service.sign;

import com.chatboard.etude.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

// We are only examining TokenService not the other objects which has dependency upon it.
// So we only provide test environment which is implemented by Mockito framework.
@ExtendWith(MockitoExtension.class)
public class OldTokenServiceTest {

    // Make fake object with dependency.
    @InjectMocks
    TokenService tokenService;
    // inject fake object for @InjectMocks-ed object.
    @Mock
    JwtHandler jwtHandler;

    @BeforeEach
    void beforeEach() {
        // inject random value to the field of target object. (object, field, value)
        ReflectionTestUtils.setField(tokenService, "accessTokenMaxAgeSeconds", 10L);
        ReflectionTestUtils.setField(tokenService, "refreshTokenMaxAgeSeconds", 10L);
        ReflectionTestUtils.setField(tokenService, "accessKey", "accessKey");
        ReflectionTestUtils.setField(tokenService, "refreshKey", "refreshKey");
    }

    @Test
    void createAccessTokenTest() {
        // given : designate an action for mock object
        // willReturn : designate a return value of the action
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("access");

        // when
        String token = tokenService.createAccessToken("subject");

        // verify : check if desired action has been taken to the object for any values
        assertThat(token).isEqualTo("access");
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
    }

    @Test
    void createRefreshTokenTest() {
        // given
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("refresh");

        // when
        String token = tokenService.createRefreshToken("subject");

        //then
        assertThat(token).isEqualTo("refresh");
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
    }

    @Test
    void validateAccessTokenTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(true);

        // when, then
        assertThat(tokenService.validateAccessToken("token")).isTrue();
    }

    @Test
    void validateRefreshTokenTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(true);

        // when, then
        assertThat(tokenService.validateRefreshToken("token")).isTrue();
    }

    @Test
    void invalidateAccessTokenTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(false);

        // when, then
        assertThat(tokenService.validateAccessToken("token")).isFalse();
    }

    @Test
    void invalidateRefreshTokenTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(false);

        // when, then
        assertThat(tokenService.validateRefreshToken("token")).isFalse();
    }

    @Test
    void extractAccessTokenSubjectTest() {
        // given
        String subject = "subject";
        given(jwtHandler.extractSubject(anyString(), anyString())).willReturn(subject);

        // when
        String result = tokenService.extractAccessTokenSubject("token");

        // then
        assertThat(subject).isEqualTo(result);
    }

    @Test
    void extractRefreshTokenSubjectTest() {
        // given
        String subject = "subject";
        given(jwtHandler.extractSubject(anyString(), anyString())).willReturn(subject);

        // when
        String result = tokenService.extractRefreshTokenSubject("token");

        // then
        assertThat(subject).isEqualTo(result);
    }

}
