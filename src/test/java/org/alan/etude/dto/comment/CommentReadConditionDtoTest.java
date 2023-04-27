package org.alan.etude.dto.comment;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.alan.etude.factory.dto.CommentReadConditionFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentReadConditionDtoTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        CommentReadConditionDto condition = CommentReadConditionFactory.createCommentReadCondition();

        // when
        Set<ConstraintViolation<CommentReadConditionDto>> violations = validator.validate(condition);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByNegativePostIdTest() {
        // given
        Long invalidValue = -1L;
        CommentReadConditionDto condition = CommentReadConditionFactory.createCommentReadCondition(invalidValue);

        // when
        Set<ConstraintViolation<CommentReadConditionDto>> violations = validator.validate(condition);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }
}
