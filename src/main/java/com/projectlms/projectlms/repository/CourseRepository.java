package com.projectlms.projectlms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "SELECT * FROM M_COURSE c WHERE c.is_deleted = false AND c.id = ?", nativeQuery = true)
    Optional<Course> searchCourseById(Long id);
}
