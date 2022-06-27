package com.projectlms.projectlms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Tool;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface ToolRepository extends SoftDeletesRepository<Tool, Long> {
    @Query(value = "SELECT * FROM M_TOOL t WHERE t.deleted_at IS NULL AND t.course_id = ?", nativeQuery = true)
    List<Tool> searchAll(Long courseId);

    @Query(value = "SELECT * FROM M_TOOL t WHERE t.deleted_at IS NULL AND t.id = ? AND t.course_id = ?", nativeQuery = true)
    Optional<Tool> searchById(Long id, Long courseId);
}