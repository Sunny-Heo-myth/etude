package org.alan.etude.dto.sign;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.alan.etude.factory.dto.SignUpRequestFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SignUpRequestDtoValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequest();

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByNotFormattedEmailTest() {
        // given
        String invalidValue = "email";
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

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
        SignUpRequestDto request = SignUpRequestFactory.createSignUpRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

}
