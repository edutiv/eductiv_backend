package com.projectlms.projectlms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    // @Query(value = "SELECT * FROM M_USER u WHERE u.is_deleted = false AND u.id = ?", nativeQuery = true)
    // Optional<User> searchUserById(Long id);

    //User findByUsername(String username);
    Optional<User> findByUsername(String username);
    //Boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM M_USER u WHERE username = ?", nativeQuery = true)
    User findUsername(String username);
}
