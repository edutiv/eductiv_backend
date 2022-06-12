package com.projectlms.projectlms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dto.CourseDto;
import com.projectlms.projectlms.repository.CategoryRepository;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<Object> addCourse(CourseDto request) {
        try {
            log.info("Save new course: {}", request);

            log.info("Find category by category id");
            Optional<Category> category = categoryRepository.findOne(request.getCategoryId());
            if(category.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

            Course course = Course.builder()
                .courseName(request.getCourseName())
                .courseImage(request.getCourseImage())
                .category(category.get())
                .description(request.getDescription())
                .totalVideo(request.getTotalVideo())
                .totalTimes(request.getTotalTimes())
                .build();

            course = courseRepository.save(course);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, course, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing create new course, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllCourse() {
        try {
            log.info("Get all course");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, courseRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by get all courses, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getCourseDetail(Long id) {
        try {
            log.info("Find course detail by course id: {}", id);
            Optional<Course> courseDetail = courseRepository.findOne(id);
            if (courseDetail.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, courseDetail.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing get course by id, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateCourse(CourseDto request, Long id) {
        try {
            log.info("Update course: {}", request);
            Optional<Course> course = courseRepository.findOne(id);
            if (course.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Find category by category id");
            Optional<Category> category = categoryRepository.findOne(request.getCategoryId());
            if(category.isEmpty()) {
                log.info("category not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            course.get().setCourseName(request.getCourseName());
            course.get().setCourseImage(request.getCourseImage());
            course.get().setCategory(category.get());
            course.get().setDescription(request.getDescription());
            course.get().setTotalVideo(request.getTotalVideo());
            course.get().setTotalTimes(request.getTotalTimes());
            courseRepository.save(course.get());
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, course.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by update course, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteCourse(Long id) {
        try {
            log.info("Executing delete course by id: {}", id);
            courseRepository.delete(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Data not found. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }
}
