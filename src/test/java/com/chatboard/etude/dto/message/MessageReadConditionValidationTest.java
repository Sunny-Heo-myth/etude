package com.chatboard.etude.dto.message;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static com.chatboard.etude.factory.dto.MessageReadConditionFactory.createMessageReadCondition;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageReadConditionValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<MessageReadCondition>> violations;
    MessageReadCondition condition;

    @Test
    void validateTest() {
        //given
        condition = createMessageReadCondition(null, 1L, 1);

        // when
        violations = validator.validate(condition);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByNotNullMemberIdTest() {
        // given
        Long invalidValue = 1L;
        condition = createMessageReadCondition(invalidValue, 1L, 1);

        // when
        violations = validator.validate(condition);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void invalidateByNullSizeTest() {
        // given
        Integer invalidValue = null;
        condition = createMessageReadCondition(null, 1L, invalidValue);

        // when
        violations = validator.validate(condition);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNegativeOrZeroSizeTest() {
        // given
        Integer invalidValue = 0;
        condition = createMessageReadCondition(null, 1L, invalidValue);

        // when
        violations = validator.validate(condition);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }
}
