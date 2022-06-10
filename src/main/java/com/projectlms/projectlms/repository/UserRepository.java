package com.projectlms.projectlms.repository;

import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface UserRepository extends SoftDeletesRepository<User, Long>{
    
}
