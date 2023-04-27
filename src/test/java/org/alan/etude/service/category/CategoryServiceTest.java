package org.alan.etude.service.category;

import org.alan.etude.dto.category.CategoryCreateRequestDto;
import org.alan.etude.dto.category.CategoryDto;
import org.alan.etude.exception.notFoundException.CategoryNotFoundException;
import org.alan.etude.factory.entity.CategoryFactory;
import org.alan.etude.repository.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.alan.etude.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static org.alan.etude.factory.entity.CategoryFactory.createCategory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;
    @Mock
    CategoryRepository categoryRepository;

    @Test
    void readAllTest() {
        // given
        given(categoryRepository.findAllOrderByParentIdAscNullsFirstCateGoryIdAsc())
                .willReturn(
                        List.of(CategoryFactory.createCategoryWithName("name1"),
                                CategoryFactory.createCategoryWithName("name2"))
                );

        // when
        List<CategoryDto> result = categoryService.readAllCategory();

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("name1");
        assertThat(result.get(1).getName()).isEqualTo("name2");
    }

    @Test
    void createTest() {
        // given
        CategoryCreateRequestDto request = createCategoryCreateRequest();

        // when
        categoryService.createCategory(request);

        // then
        verify(categoryRepository).save(any());
    }

    @Test
    void deleteTest() {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));

        // when
        categoryService.deleteCategory(1L);

        // then
        verify(categoryRepository).delete(any());
    }

    @Test
    void deleteExceptionByCategoryNotFoundTest() {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                .isInstanceOf(CategoryNotFoundException.class);
    }
}
