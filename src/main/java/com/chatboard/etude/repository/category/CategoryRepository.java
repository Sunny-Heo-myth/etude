package com.chatboard.etude.repository.category;

import com.chatboard.etude.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // if p.id is null it is root.
    //
    @Query("select c " +
            "from Category c left join c.parent p " +
            "order by p.id asc nulls first , c.id asc")
    List<Category> findAllOrderByParentIdAscNullsFirstCateGoryIdAsc();
}
