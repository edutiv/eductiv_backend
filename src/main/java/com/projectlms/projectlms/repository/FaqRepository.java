package com.projectlms.projectlms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long>{
    
}
