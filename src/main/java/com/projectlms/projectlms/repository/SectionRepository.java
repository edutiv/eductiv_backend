package com.projectlms.projectlms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    
    @Query(value = "SELECT * FROM M_SECTION s WHERE s.is_deleted = false AND s.course_id = ?", nativeQuery = true)
    List<Section> searchAllSections(Long courseId);

    @Query(value = "SELECT * FROM M_SECTION s WHERE s.is_deleted = false AND s.id = ? AND s.course_id = ?", nativeQuery = true)
    Optional<Section> searchSectionById(Long id, Long courseId);

    @Modifying
    @Query(value = "UPDATE M_SECTION SET is_deleted = true WHERE course_id = ?", nativeQuery = true)
    void deleteSectionByCourse(Long id);
}
