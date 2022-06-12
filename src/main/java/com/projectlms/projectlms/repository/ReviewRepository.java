package com.projectlms.projectlms.repository;

import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Review;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface ReviewRepository extends SoftDeletesRepository<Review, Long> {
    
}
