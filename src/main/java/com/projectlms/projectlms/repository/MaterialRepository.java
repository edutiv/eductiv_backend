package com.projectlms.projectlms.repository;

import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Material;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface MaterialRepository extends SoftDeletesRepository<Material, Long>{
    
}
