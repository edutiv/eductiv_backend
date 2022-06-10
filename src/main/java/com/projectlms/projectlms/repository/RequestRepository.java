package com.projectlms.projectlms.repository;

import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Request;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface RequestRepository extends SoftDeletesRepository<Request, Long>{
    
}