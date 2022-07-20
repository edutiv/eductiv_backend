package com.projectlms.projectlms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "SELECT * FROM M_REPORT r WHERE r.is_deleted = false AND r.enrolled_course_id = ? AND r.material_id = ?", nativeQuery = true)
    Report getReport(Long enrolledCourseId, Long materialId);

    @Query(value = "SELECT * FROM M_REPORT r WHERE r.is_deleted = false AND r.enrolled_course_id = ? AND r.is_completed = true", nativeQuery = true)
    List<Report> findByCompleted(Long enrolledCourseId);
    
}
