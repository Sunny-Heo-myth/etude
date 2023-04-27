package org.alan.etude.dto.post;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.alan.etude.factory.dto.PostUpdateRequestFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PostUpdateRequestDtoValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        PostUpdateRequestDto request = PostUpdateRequestFactory.createPostUpdateRequest("title", "content", 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequestDto>> validate = validator.validate(request);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyTitleTest() {
        // given
        String invalidValue = null;
        PostUpdateRequestDto request = PostUpdateRequestFactory.createPostUpdateRequest(invalidValue, "content", 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);

    }

    @Test
    void invalidateByBlankTitleTest() {
        // given
        String invalidValue = " ";
        PostUpdateRequestDto request = PostUpdateRequestFactory.createPostUpdateRequest(invalidValue, "content", 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByEmptyContentTest() {
        // given
        String invalidValue = null;
        PostUpdateRequestDto request = PostUpdateRequestFactory.createPostUpdateRequest("title", invalidValue, 4321L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByBlackContentTest() {
        // given
        String invalidValue = " ";
        PostUpdateRequestDto request = PostUpdateRequestFactory.createPostUpdateRequest("title", invalidValue, 4321L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByEmptyPriceTest() {
        // given
        Long invalidValue = null;
        PostUpdateRequestDto request = PostUpdateRequestFactory.createPostUpdateRequest("title", "content", invalidValue, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }

    @Test
    void invalidateByNegativePriceTest() {
        // given
        Long invalidValue = -1L;
        PostUpdateRequestDto request = new PostUpdateRequestDto("title", "content", invalidValue, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequestDto>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }
}
