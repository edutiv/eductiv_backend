package com.projectlms.projectlms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    // @Query(value = "SELECT * FROM M_USER u WHERE u.is_deleted = false AND u.id = ?", nativeQuery = true)
    // Optional<User> searchUserById(Long id);
    // Optional<User> findByUsername(String username);

    // Boolean existsByUsername(String username);
}
