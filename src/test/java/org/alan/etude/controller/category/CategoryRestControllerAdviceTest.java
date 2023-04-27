package org.alan.etude.controller.category;

import org.alan.etude.advice.ExceptionAdvice;
import org.alan.etude.exception.CannotConvertNestedStructureException;
import org.alan.etude.exception.notFoundException.CategoryNotFoundException;
import org.alan.etude.handler.FailureResponseHandler;
import org.alan.etude.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryRestControllerAdviceTest {
    @InjectMocks
    CategoryRestController categoryController;
    @Mock
    CategoryService categoryService;
    @Mock
    FailureResponseHandler failureResponseHandler;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasenames("i18n/exception");

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new ExceptionAdvice(failureResponseHandler))
                .build();
    }

    @Test
    void readAllTest() throws Exception {
        // given
        given(categoryService.readAllCategory()).willThrow(CannotConvertNestedStructureException.class);

        // when, then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteTest() throws Exception {
        // given
        doThrow(CategoryNotFoundException.class).when(categoryService).deleteCategory(anyLong());

        // when, then
        mockMvc.perform(delete("/api/categories/{id}", 1L))
                .andExpect(status().isNotFound());
    }

}
