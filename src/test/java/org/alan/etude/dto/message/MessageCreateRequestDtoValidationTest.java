package org.alan.etude.dto.message;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.alan.etude.factory.dto.MessageCreateRequestFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCreateRequestDtoValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    MessageCreateRequestDto request = MessageCreateRequestFactory.createMessageCreateRequest("content", null, 2L);
    Set<ConstraintViolation<MessageCreateRequestDto>> violations;

    @Test
    void validateTest() {
        // given
        request = MessageCreateRequestFactory.createMessageCreateRequest("content", null, 2L);

        // when
        violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByEmptyContentTest() {
        // given
        String invalidValue = null;
        request = MessageCreateRequestFactory.createMessageCreateRequest(invalidValue, null, 2L);

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
        request = MessageCreateRequestFactory.createMessageCreateRequest(invalidValue, null, 2L);

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
        request = MessageCreateRequestFactory.createMessageCreateRequest("content", invalidValue, 2L);

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
        request = MessageCreateRequestFactory.createMessageCreateRequest("content", null, invalidValue);

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
        request = MessageCreateRequestFactory.createMessageCreateRequest("content", 1L, invalidValue);

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
