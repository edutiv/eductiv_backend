package com.projectlms.projectlms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Section;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface SectionRepository extends SoftDeletesRepository<Section, Long> {
    
    @Query(value = "SELECT * FROM M_SECTION s WHERE s.course_id = ?", nativeQuery = true)
    List<Section> searchAll(Long courseId);

    @Query(value = "SELECT * FROM M_SECTION s WHERE s.id = ? AND s.course_id = ?", nativeQuery = true)
    Optional<Section> searchById(Long id, Long courseId);
}
