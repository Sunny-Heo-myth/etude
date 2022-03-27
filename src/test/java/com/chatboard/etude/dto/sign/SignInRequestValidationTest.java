package com.chatboard.etude.dto.sign;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SignInRequestValidationTest {

    // dependency

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // auxiliary

    private SignInRequest createRequest() {
        return new SignInRequest("email@email.com", "123456a!");
    }

    private SignInRequest createRequestWithEmail(String email) {
        return new SignInRequest(email, "123456a!");
    }

    private SignInRequest createRequestWithPassword(String password) {
        return new SignInRequest("email@email.com", password);
    }

    // test

    @Test
    void validateTest() {
        // given
        SignInRequest request = createRequest();

        // when
        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByNotFormattedEmailTest() {
        // given
        String invalidValue = "email";
        SignInRequest request = createRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidByEmptyEmailTest() {
        // given
        String invalidValue = null;
        SignInRequest request = createRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidByBlankEmailTest() {
        // given
        String invalidValue = " ";
        SignInRequest request = createRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidByEmptyPasswordTest() {
        // given
        String invalidValue = null;
        SignInRequest request = createRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidByBlankPasswordTest() {
        // given
        String invalidValue = " ";
        SignInRequest request = createRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }
}
