package org.alan.etude.service.category;

import org.alan.etude.dto.category.CategoryCreateRequestDto;
import org.alan.etude.dto.category.CategoryDto;
import org.alan.etude.entity.category.Category;
import org.alan.etude.exception.notFoundException.CategoryNotFoundException;
import org.alan.etude.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> readAllCategory() {
        List<Category> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCateGoryIdAsc();
        return CategoryDto.toDtoList(categories);
    }

    @Transactional
    public void createCategory(CategoryCreateRequestDto request) {

        // Optional for when it is a root category :
        Category parent = Optional.ofNullable(request.getParentId())
                .map(parentId -> categoryRepository.findById(parentId)
                        // The parent category id exists but could not find in DB.
                        .orElseThrow(CategoryNotFoundException::new))
                // This category is a root category.
                .orElse(null);

        categoryRepository.save(new Category(request.getName(), parent));
    }

    @Transactional
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        categoryRepository.delete(category);
    }

}
