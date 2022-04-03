package com.chatboard.etude.repository.category;

import com.chatboard.etude.config.etudeTest.TestConfiguration;
import com.chatboard.etude.entity.category.Category;
import com.chatboard.etude.exception.CategoryNotFoundException;
import com.chatboard.etude.factory.entity.CategoryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.chatboard.etude.factory.entity.CategoryFactory.createCategory;
import static com.chatboard.etude.factory.entity.CategoryFactory.createCategoryWithName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(TestConfiguration.class)
public class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Test
    void createAndReadTest() {
        // given
        Category category = createCategory();

        // when
        Category savedCategory = categoryRepository.save(category);
        clear();

        // then
        Category foundCategory = categoryRepository.findById(savedCategory.getId())
                .orElseThrow(CategoryNotFoundException::new);
        assertThat(foundCategory.getId()).isEqualTo(savedCategory.getId());
    }

    @Test
    void readAllTest() {
        // given
        List<Category> categories = Stream.of("name1", "name2", "name3")
                .map(CategoryFactory::createCategoryWithName)
                .collect(Collectors.toList());
        categoryRepository.saveAll(categories);
        clear();

        // when
        List<Category> foundCategories = categoryRepository.findAll();

        // then
        assertThat(foundCategories.size()).isEqualTo(3);
    }

    @Test
    void deleteTest() {
        // given
        Category category = categoryRepository.save(createCategory());
        clear();

        // when
        categoryRepository.delete(category);
        clear();

        // then
        assertThatThrownBy(() -> categoryRepository.findById(category.getId())
                .orElseThrow(CategoryNotFoundException::new ))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void deleteCascadeTest() {
        // given
        Category category1 = categoryRepository.save(createCategoryWithName("category1"));
        Category category2 = categoryRepository.save(createCategory("category2", category1));
        Category category3 = categoryRepository.save(createCategory("category3", category2));
        Category category4 = categoryRepository.save(createCategoryWithName("category4"));
        clear();

        // when
        categoryRepository.deleteById(category1.getId());
        clear();

        // then
        List<Category> result = categoryRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(category4.getId());
    }

    @Test
    void deleteNoneValueTest() {
        // given
        Long nonValueId = 100L;

        // when, then
        assertThatThrownBy(() -> categoryRepository.deleteById(nonValueId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    private void clear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findAllWithParentOrderByParentIdAscNullsFirstCategoryIdAscTest() {
        // given

        // 1    null
        // 2    1
        // 3    1
        // 4    2
        // 5    2
        // 6    4
        // 7    3
        // 8    null
        Category category1 = categoryRepository.save(createCategory("category1", null));
        Category category2 = categoryRepository.save(createCategory("category2", category1));
        Category category3 = categoryRepository.save(createCategory("category3", category1));
        Category category4 = categoryRepository.save(createCategory("category4", category2));
        Category category5 = categoryRepository.save(createCategory("category5", category2));
        Category category6 = categoryRepository.save(createCategory("category6", category4));
        Category category7 = categoryRepository.save(createCategory("category7", category3));
        Category category8 = categoryRepository.save(createCategory("category8", null));
        clear();

        // when
        List<Category> result = categoryRepository.findAllOrderByParentIdAscNullsFirstCateGoryIdAsc();

        // then

        // 1    null
        // 8    null
        // 2    1
        // 3    1
        // 4    2
        // 5    2
        // 7    3
        // 6    4
        assertThat(result.size()).isEqualTo(8);
        assertThat(result.get(0).getId()).isEqualTo(category1.getId());
        assertThat(result.get(1).getId()).isEqualTo(category8.getId());
        assertThat(result.get(2).getId()).isEqualTo(category2.getId());
        assertThat(result.get(3).getId()).isEqualTo(category3.getId());
        assertThat(result.get(4).getId()).isEqualTo(category4.getId());
        assertThat(result.get(5).getId()).isEqualTo(category5.getId());
        assertThat(result.get(6).getId()).isEqualTo(category7.getId());
        assertThat(result.get(7).getId()).isEqualTo(category6.getId());

    }


}
