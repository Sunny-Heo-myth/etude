package org.alan.etude.dto.post;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

public class PostCreateRequestDtoValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        //PostCreateRequest request = createP
    }

}
