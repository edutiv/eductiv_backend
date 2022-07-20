package com.projectlms.projectlms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.EnrolledCourse;

@Repository
public interface EnrolledCourseRepository extends JpaRepository<EnrolledCourse, Long>{
    @Query(value = "SELECT ec.* FROM M_ENROLLED_COURSE ec INNER JOIN M_USER u ON ec.user_id=u.id WHERE ec.is_deleted = false AND ec.user_id = ?", nativeQuery = true)
    List<EnrolledCourse> getEnrolledCourseByUser(Long id);

    @Query(value = "SELECT ec.* FROM M_ENROLLED_COURSE ec INNER JOIN M_COURSE c ON ec.course_id=c.id WHERE ec.is_deleted = false AND ec.course_id = ?", nativeQuery = true)
    List<EnrolledCourse> getEnrolledCourseByCourse(Long id);

    @Query(value = "SELECT COUNT(ec.rating) FROM M_ENROLLED_COURSE ec WHERE ec.course_id = ? AND ec.is_deleted = false", nativeQuery = true)
    Integer countRatingByCourse(Long id);

    @Query(value = "SELECT SUM(ec.rating) FROM M_ENROLLED_COURSE ec WHERE ec.course_id = ? AND ec.is_deleted = false", nativeQuery = true)
    Double sumRatingByCourse(Long id);

}