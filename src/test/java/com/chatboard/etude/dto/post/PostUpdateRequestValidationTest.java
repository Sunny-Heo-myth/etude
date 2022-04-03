package com.chatboard.etude.dto.post;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.chatboard.etude.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;

public class PostUpdateRequestValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        PostUpdateRequest request = createPostUpdateRequest("title", "content", 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(request);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyTitleTest() {
        // given
        String invalidValue = null;
        PostUpdateRequest request = createPostUpdateRequest(invalidValue, "content", 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> violations = validator.validate(request);

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
        PostUpdateRequest request = createPostUpdateRequest(invalidValue, "content", 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> violations = validator.validate(request);

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
        PostUpdateRequest request = createPostUpdateRequest("title", invalidValue, 4321L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> violations = validator.validate(request);

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
        PostUpdateRequest request = createPostUpdateRequest("title", invalidValue, 4321L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> violations = validator.validate(request);

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
        PostUpdateRequest request = createPostUpdateRequest("title", "content", invalidValue, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> violations = validator.validate(request);

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
        PostUpdateRequest request = new PostUpdateRequest("title", "content", invalidValue, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .map(ConstraintViolation::getInvalidValue)
                .collect(Collectors.toSet()))
                .contains(invalidValue);
    }
}
