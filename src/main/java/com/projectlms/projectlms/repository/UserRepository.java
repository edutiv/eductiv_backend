package com.projectlms.projectlms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM M_USER u WHERE username = ? AND u.is_deleted = false", nativeQuery = true)
    User findUsername(String username);
}
