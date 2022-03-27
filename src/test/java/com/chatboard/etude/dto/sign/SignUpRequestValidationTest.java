package com.chatboard.etude.dto.sign;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SignUpRequestValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private SignUpRequest createRequest() {
        return new SignUpRequest("email@email.com", "123456a!", "username", "nickname");
    }

    private SignUpRequest createRequestWithEmail(String email) {
        return new SignUpRequest(email, "123456a!", "username", "nickname");
    }

    private SignUpRequest createRequestWithPassword(String password) {
        return new SignUpRequest("email@email.com", password, "username", "nickname");
    }

    private SignUpRequest createRequestWithUsername(String username) {
        return new SignUpRequest("email@email.com", "123456a!", username, "nickname");
    }

    private SignUpRequest createRequestWithNickName(String nickname) {
        return new SignUpRequest("email@email.com", "123456a!", "username", nickname);
    }


    @Test
    void validateTest() {
        // given
        SignUpRequest request = createRequest();

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByNotFormattedEmailTest() {
        // given
        String invalidValue = "email";
        SignUpRequest request = createRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByEmptyEmailTest() {
        // given
        String invalidValue = null;
        SignUpRequest request = createRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByEmptyPasswordTest() {
        // given
        String invalidValue = null;
        SignUpRequest request = createRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByBlankPasswordTest() {
        // given
        String invalidValue = "    ";
        SignUpRequest request = createRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByShortPasswordTest() {
        // given
        String invalidValue = "123";
        SignUpRequest request = createRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNoneAlphabetPasswordTest() {
        // given
        String invalidValue = "123!@#1312";
        SignUpRequest request = createRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNoneNumberPasswordTest() {
        // given
        String invalidValue = "adjaf!@dsfss";
        SignUpRequest request = createRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNoneSpecialCasePasswordTest() {
        // given
        String invalidValue = "adfa142321";
        SignUpRequest request = createRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByEmptyUsernameTest() {
        // given
        String invalidValue = null;
        SignUpRequest request = createRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByBlankUsernameTest() {
        // given
        String invalidValue = " ";
        SignUpRequest request = createRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByShortUsernameTest() {
        // given
        String invalidValue = "허";
        SignUpRequest request = createRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNotAlphabetOrKoreanTest() {
        // given
        String invalidValue = "허2dfad";
        SignUpRequest request = createRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByEmptyNicknameTest() {
        // given
        String invalidValue = null;
        SignUpRequest request = createRequestWithNickName(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByBlankNicknameTest() {
        // given
        String invalidValue = "  ";
        SignUpRequest request = createRequestWithNickName(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByShortNicknameTest() {
        // given
        String invalidValue = "허";
        SignUpRequest request = createRequestWithNickName(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNotAlphabetOrKoreanNicknameTest() {
        // given
        String invalidValue = " ";
        SignUpRequest request = createRequestWithNickName(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

}
