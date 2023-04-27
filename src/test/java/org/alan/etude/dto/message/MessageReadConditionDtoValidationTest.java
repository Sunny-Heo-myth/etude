package org.alan.etude.dto.message;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.alan.etude.factory.dto.MessageReadConditionFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageReadConditionDtoValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<MessageReadConditionDto>> violations;
    MessageReadConditionDto condition;

    @Test
    void validateTest() {
        //given
        condition = MessageReadConditionFactory.createMessageReadCondition(null, 1L, 1);

        // when
        violations = validator.validate(condition);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByNotNullMemberIdTest() {
        // given
        Long invalidValue = 1L;
        condition = MessageReadConditionFactory.createMessageReadCondition(invalidValue, 1L, 1);

        // when
        violations = validator.validate(condition);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void invalidateByNullSizeTest() {
        // given
        Integer invalidValue = null;
        condition = MessageReadConditionFactory.createMessageReadCondition(null, 1L, invalidValue);

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
        condition = MessageReadConditionFactory.createMessageReadCondition(null, 1L, invalidValue);

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
