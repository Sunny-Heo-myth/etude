package com.chatboard.etude.dto.post;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

public class PostCreateRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        //PostCreateRequest request = createP
    }

}
