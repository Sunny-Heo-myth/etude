package org.alan.etude.dto.category;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.alan.etude.factory.dto.CategoryCreateRequestFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryCreateRequestDtoValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        CategoryCreateRequestDto request = CategoryCreateRequestFactory.createCategoryCreateRequest();

        // when
        Set<ConstraintViolation<CategoryCreateRequestDto>> validate = validator.validate(request);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyNameTest() {
        // given
        String invalidValue = null;
        CategoryCreateRequestDto request = CategoryCreateRequestFactory.createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequestDto>> validate = validator.validate(request);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet())
                .contains(invalidValue)
        ).isTrue();
    }

    @Test
    void invalidateByBlankNameTest() {
        // given
        String invalidValue = " ";
        CategoryCreateRequestDto req = CategoryCreateRequestFactory.createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequestDto>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByShortNameTest() {
        // given
        String invalidValue = "c";
        CategoryCreateRequestDto req = CategoryCreateRequestFactory.createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequestDto>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByLongNameTest() {
        // given
        String invalidValue = "c".repeat(50);

        CategoryCreateRequestDto req = CategoryCreateRequestFactory.createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequestDto>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }
}
