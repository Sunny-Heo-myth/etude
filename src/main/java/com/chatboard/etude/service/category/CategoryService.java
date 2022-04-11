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
import java.util.Optional;

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

        // Optional for when it is a root category :
        Category parent = Optional.ofNullable(request.getParentId())
                .map(parentId -> categoryRepository.findById(parentId)

                        // The parent id exists but could not find in DB.
                        .orElseThrow(CategoryNotFoundException::new))

                // This category is a root category.
                .orElse(null);
        categoryRepository.save(new Category(request.getName(), parent));
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
    }

}
