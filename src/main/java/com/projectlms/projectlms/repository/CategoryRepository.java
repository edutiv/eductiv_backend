package com.projectlms.projectlms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM M_CATEGORY c WHERE c.is_deleted = false AND c.id = ?", nativeQuery = true)
    Optional<Category> searchCategoryById(Long id);
}
