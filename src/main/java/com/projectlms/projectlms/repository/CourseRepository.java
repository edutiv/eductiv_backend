package com.projectlms.projectlms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    //search course berdasarkan id course
    @Query(value = "SELECT * FROM M_COURSE c WHERE c.is_deleted = false AND c.id = ?", nativeQuery = true)
    Optional<Course> searchCourseById(Long id);

    //search course berdasarkan category/specialization
    @Query(value = "SELECT * from M_COURSE c WHERE c.category_id = ?", nativeQuery = true)
    List<Course> searchCourseByCategory(Long id);

    //searching course
    @Query(value = "SELECT * FROM M_COURSE c WHERE lower(c.course_name) like lower(concat('%', :course_name,'%'))", nativeQuery = true)
    List<Course> searchByCourseName(String course_name);
    
    //hapus course berdasarkan category
}
