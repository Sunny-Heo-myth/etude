package com.chatboard.etude.service.category;

import com.chatboard.etude.dto.category.CategoryCreateRequest;
import com.chatboard.etude.dto.category.CategoryDto;
import com.chatboard.etude.entity.category.Category;
import com.chatboard.etude.exception.CategoryNotFoundException;
import com.chatboard.etude.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> readAll() {
        List<Category> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCateGoryIdAsc();
        return CategoryDto.toDtoList(categories);
    }

    @Transactional
    public void create(CategoryCreateRequest request) {
        categoryRepository.save(CategoryCreateRequest.toEntity(request, categoryRepository ));
    }

    @Transactional
    public void delete(Long id) {
        if (notExistsCategory(id)) {
            throw new CategoryNotFoundException();
        }
        categoryRepository.deleteById(id);
    }

    private boolean notExistsCategory(Long id) {
        return !categoryRepository.existsById(id);
    }
}
