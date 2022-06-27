package com.projectlms.projectlms.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface UserRepository extends SoftDeletesRepository<User, Long>{
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
