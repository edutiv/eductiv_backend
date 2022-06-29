package com.projectlms.projectlms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    @Query(value = "SELECT * FROM M_REVIEW r WHERE r.is_deleted = false AND r.course_id = ?", nativeQuery = true)
    List<Review> searchAllReviews(Long courseId);

    @Query(value = "SELECT * FROM M_REVIEW r WHERE r.is_deleted = false AND r.id = ? AND r.course_id = ?", nativeQuery = true)
    Optional<Review> searchReviewById(Long id, Long courseId);

    @Modifying
    @Query(value = "UPDATE M_REVIEW SET is_deleted = true WHERE course_id = ?", nativeQuery = true)
    void deleteReviewByCourse(Long id);
}
