package org.alan.etude.dto.post;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.alan.etude.factory.dto.PostReadConditionFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PostReadConditionDtoValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        PostReadConditionDto postReadConditionDto = PostReadConditionFactory.createPostReadCondition(1, 1);

        // when
        Set<ConstraintViolation<PostReadConditionDto>> violations = validator.validate(postReadConditionDto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidateByNullPageTest() {
        // given
        Integer invalidValue = null;
        PostReadConditionDto req = PostReadConditionFactory.createPostReadCondition(invalidValue, 1);

        // when
        Set<ConstraintViolation<PostReadConditionDto>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNegativePageTest() {
        // given
        Integer invalidValue = -1;
        PostReadConditionDto req = PostReadConditionFactory.createPostReadCondition(invalidValue, 1);

        // when
        Set<ConstraintViolation<PostReadConditionDto>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNullSizeTest() {
        // given
        Integer invalidValue = null;
        PostReadConditionDto req = PostReadConditionFactory.createPostReadCondition(1, invalidValue);

        // when
        Set<ConstraintViolation<PostReadConditionDto>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNegativeOrZeroPageTest() {
        // given
        Integer invalidValue = 0;
        PostReadConditionDto req = PostReadConditionFactory.createPostReadCondition(1, invalidValue);

        // when
        Set<ConstraintViolation<PostReadConditionDto>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }
}
