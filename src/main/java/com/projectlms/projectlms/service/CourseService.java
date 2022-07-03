package com.projectlms.projectlms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.domain.dto.CourseDto;
import com.projectlms.projectlms.repository.CategoryRepository;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.ReviewRepository;
import com.projectlms.projectlms.repository.SectionRepository;
import com.projectlms.projectlms.repository.ToolRepository;
import com.projectlms.projectlms.repository.UserRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final SectionRepository sectionRepository;
    private final ToolRepository toolRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository, SectionRepository sectionRepository, ToolRepository toolRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.sectionRepository = sectionRepository;
        this.toolRepository = toolRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> addCourse(CourseDto request) {
        try {
            log.info("Save new course: {}", request);

            log.info("Find category by category id");
            Optional<Category> category = categoryRepository.searchCategoryById(request.getCategoryId());
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
            List<Course> courses = courseRepository.findAll();
            if (courses.isEmpty()) {
                log.info("courses is empty");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, courses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by get all courses, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getCourseDetail(Long id) {
        try {
            log.info("Find course detail by course id: {}", id);
            Optional<Course> courseDetail = courseRepository.searchCourseById(id);
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
            Optional<Course> course = courseRepository.searchCourseById(id);
            if (course.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Find category by category id");
            Optional<Category> category = categoryRepository.searchCategoryById(request.getCategoryId());
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
            courseRepository.deleteById(id);
            sectionRepository.deleteSectionByCourse(id);
            toolRepository.deleteToolByCourse(id);
            reviewRepository.deleteReviewByCourse(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Data not found. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCourseByUserSpecialization(String email) {
        try {
            log.info("Get user");
            User user = userRepository.findUsername(email);
            if(user.getCategory() == null) throw new Exception("User don't have specialization");
            
            log.info("Get courses by user specialization");
            List<Course> courses = courseRepository.searchCourseByCategory(user.getCategory().getId());
            if (courses.isEmpty()) {
                log.info("courses with category " + user.getCategory().getCategoryName() + " is empty");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, courses, HttpStatus.OK);
        
        } catch (Exception e) {
            log.error("Get an error by get course by user specialization, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> searchByCourseName(String course_name) {
        try {
            log.info("Get courses by course name");
            List<Course> courses = courseRepository.searchByCourseName(course_name);
            if (courses.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, courses, HttpStatus.OK);
        
        } catch (Exception e) {
            log.error("Get an error by searching course by name, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // public ResponseEntity<Object> getCourseByCourseName(String course_name) {
    //     try {
    //         log.info("Get courses by course name");
    //         List<Course> courses = courseRepository.findByName(course_name);
    //         if (courses.isEmpty()) {
    //             log.info("course not found");
    //             return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
    //         }
    //         return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, courses, HttpStatus.OK);
        
    //     } catch (Exception e) {
    //         log.error("Get an error by searching course by name, Error : {}",e.getMessage());
    //         return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
}
