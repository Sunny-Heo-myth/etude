package org.alan.etude.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alan.etude.dto.category.CategoryCreateRequestDto;
import org.alan.etude.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.alan.etude.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryRestControllerTest {

    @InjectMocks
    CategoryRestController categoryController;
    @Mock
    CategoryService categoryService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void readAllTest() throws Exception {
        // given, when, then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());

        verify(categoryService).readAllCategory();
    }

    @Test
    void createTest() throws Exception {
        // given
        CategoryCreateRequestDto request = createCategoryCreateRequest();

        // when, then
        mockMvc.perform(
                post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(categoryService).createCategory(request);
    }

    @Test
    void deleteTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/categories/{id}", id))
                .andExpect(status().isOk());
        verify(categoryService).deleteCategory(id);
    }
}
