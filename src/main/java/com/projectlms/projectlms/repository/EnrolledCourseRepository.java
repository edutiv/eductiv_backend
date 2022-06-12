package com.projectlms.projectlms.repository;

import org.springframework.stereotype.Repository;

import com.projectlms.projectlms.domain.dao.EnrolledCourse;
import com.projectlms.projectlms.repository.softdeletes.SoftDeletesRepository;

@Repository
public interface EnrolledCourseRepository extends SoftDeletesRepository<EnrolledCourse, Long>{
    
}