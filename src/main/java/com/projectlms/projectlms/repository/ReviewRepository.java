package com.projectlms.projectlms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Review;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface ReviewRepository extends SoftDeletesRepository<Review, Long> {
    
    @Query(value = "SELECT * FROM M_REVIEW r WHERE r.deleted_at IS NULL AND r.course_id = ?", nativeQuery = true)
    List<Review> searchAll(Long courseId);

    @Query(value = "SELECT * FROM M_REVIEW r WHERE r.deleted_at IS NULL AND r.id = ? AND r.course_id = ?", nativeQuery = true)
    Optional<Review> searchById(Long id, Long courseId);
}
