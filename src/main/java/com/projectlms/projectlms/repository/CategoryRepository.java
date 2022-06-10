package com.projectlms.projectlms.repository;

import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface CategoryRepository extends SoftDeletesRepository<Category, Long> {
    
}
