package com.projectlms.projectlms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.RequestForm;

@Repository
public interface RequestRepository extends JpaRepository<RequestForm, Long> {
    
}
