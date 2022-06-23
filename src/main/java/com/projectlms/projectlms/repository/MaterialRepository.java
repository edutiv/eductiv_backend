package com.projectlms.projectlms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Material;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface MaterialRepository extends SoftDeletesRepository<Material, Long>{
    
    @Query(value = "SELECT * FROM M_MATERIAL m WHERE m.deleted_at IS NULL AND m.section_id = ?", nativeQuery = true)
    List<Material> searchAll(Long sectionId);

    @Query(value = "SELECT * FROM M_MATERIAL m WHERE m.deleted_at IS NULL AND m.id = ? AND m.section_id = ?", nativeQuery = true)
    Optional<Material> searchById(Long id, Long sectionId);
}
