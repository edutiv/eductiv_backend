package com.projectlms.projectlms.repository;

import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface CourseRepository extends SoftDeletesRepository<Course, Long> {
    
}
