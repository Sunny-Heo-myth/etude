package com.chatboard.etude.dto.sign;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static com.chatboard.etude.factory.dto.SignInRequestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SignInRequestDtoValidationTest {

    // dependency

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // test

    @Test
    void validateTest() {
        // given
        SignInRequestDto request = createSignInRequest();

        // when
        Set<ConstraintViolation<SignInRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByNotFormattedEmailTest() {
        // given
        String invalidValue = "email";
        SignInRequestDto request = createSignInRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignInRequestDto>> violations = validator.validate(request);

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
        SignInRequestDto request = createSignInRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignInRequestDto>> violations = validator.validate(request);

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
        SignInRequestDto request = createSignInRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignInRequestDto>> violations = validator.validate(request);

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
        SignInRequestDto request = createSignInRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignInRequestDto>> violations = validator.validate(request);

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
        SignInRequestDto request = createSignInRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignInRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }
}
