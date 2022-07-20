package com.projectlms.projectlms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Tool;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    @Query(value = "SELECT * FROM M_TOOL t WHERE t.is_deleted = false AND t.course_id = ?", nativeQuery = true)
    List<Tool> searchAllTools(Long courseId);

    @Query(value = "SELECT * FROM M_TOOL t WHERE t.is_deleted = false AND t.id = ? AND t.course_id = ?", nativeQuery = true)
    Optional<Tool> searchToolById(Long id, Long courseId);

    @Modifying
    @Query(value = "UPDATE M_TOOL SET is_deleted = true WHERE course_id = ?", nativeQuery = true)
    void deleteToolByCourse(Long id);
}