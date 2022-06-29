package com.projectlms.projectlms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>{
    
    @Query(value = "SELECT * FROM M_MATERIAL m WHERE m.is_deleted = false AND m.section_id = ?", nativeQuery = true)
    List<Material> searchAllMaterials(Long sectionId);

    @Query(value = "SELECT * FROM M_MATERIAL m WHERE m.is_deleted = false AND m.id = ? AND m.section_id = ?", nativeQuery = true)
    Optional<Material> searchMaterialById(Long id, Long sectionId);

    @Modifying
    @Query(value = "UPDATE M_MATERIAL SET is_deleted = true WHERE section_id = ?", nativeQuery = true)
    void deleteMaterialBySection(Long id);
}
