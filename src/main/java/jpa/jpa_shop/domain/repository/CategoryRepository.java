package jpa.jpa_shop.domain.repository;

import jpa.jpa_shop.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query(value = "select c from Category c where c.parent is null")
    List<Category> findByParentIsNull();

    @Query(value = "select c from Category c where c.parent is not null ")
    List<Category> findByParentIsNotNull();

    Optional<Category> findByName(String Name);
}
