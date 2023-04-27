package org.alan.etude.dto.sign;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.alan.etude.factory.dto.SignInRequestFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SignInRequestDtoValidationTest {

    // dependency

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // test

    @Test
    void validateTest() {
        // given
        SignInRequestDto request = SignInRequestFactory.createSignInRequest();

        // when
        Set<ConstraintViolation<SignInRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByNotFormattedEmailTest() {
        // given
        String invalidValue = "email";
        SignInRequestDto request = SignInRequestFactory.createSignInRequestWithEmail(invalidValue);

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
        SignInRequestDto request = SignInRequestFactory.createSignInRequestWithEmail(invalidValue);

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
        SignInRequestDto request = SignInRequestFactory.createSignInRequestWithEmail(invalidValue);

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
        SignInRequestDto request = SignInRequestFactory.createSignInRequestWithPassword(invalidValue);

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
        SignInRequestDto request = SignInRequestFactory.createSignInRequestWithPassword(invalidValue);

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
