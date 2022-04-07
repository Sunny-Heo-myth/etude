package com.chatboard.etude.dto.message;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static com.chatboard.etude.factory.dto.MessageCreateRequestFactory.createMessageCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageCreateRequestValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    MessageCreateRequest request = createMessageCreateRequest("content", null, 2L);
    Set<ConstraintViolation<MessageCreateRequest>> violations;

    @Test
    void validateTest() {
        // given
        request = createMessageCreateRequest("content", null, 2L);

        // when
        violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByEmptyContentTest() {
        // given
        String invalidValue = null;
        request = createMessageCreateRequest(invalidValue, null, 2L);

        // when
        violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByBlankContentTest() {
        // given
        String invalidValue = " ";
        request = createMessageCreateRequest(invalidValue, null, 2L);

        // when
        violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNotNullMemberIdTest() {
        // given
        Long invalidValue = 1L;
        request = createMessageCreateRequest("content", invalidValue, 2L);

        // when
        violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNullReceiverIdTest() {
        // given
        Long invalidValue = null;
        request = createMessageCreateRequest("content", null, invalidValue);

        // when
        violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNegativeOrZeroReceiverIdTest() {
        // given
        Long invalidValue = 0L;
        request = createMessageCreateRequest("content", 1L, invalidValue);

        // when
        violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }
}
