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
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dao.EnrolledCourse;
import com.projectlms.projectlms.domain.dao.Material;
import com.projectlms.projectlms.domain.dao.Report;
import com.projectlms.projectlms.domain.dao.RoleEnum;
import com.projectlms.projectlms.domain.dao.Section;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.domain.dto.EnrolledCourseDto;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.EnrolledCourseRepository;
import com.projectlms.projectlms.repository.ReportRepository;
import com.projectlms.projectlms.repository.SectionRepository;
import com.projectlms.projectlms.repository.UserRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EnrolledCourseService {
    
    private final EnrolledCourseRepository enrolledCourseRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final SectionRepository sectionRepository;

    @Autowired
    private ReportService reportService;

    // @Autowired
    // public EnrolledCourseService(EnrolledCourseRepository enrolledCourseRepository, CourseRepository courseRepository, UserRepository userRepository, ReportRepository reportRepository, SectionRepository sectionRepository) {
    //     this.enrolledCourseRepository = enrolledCourseRepository;
    //     this.courseRepository = courseRepository;
    //     this.userRepository = userRepository;
    //     this.reportRepository = reportRepository;
    //     this.sectionRepository = sectionRepository;
    // }

    private Material material;
    private Boolean check, checkAdmin;
    private Integer totalMaterials;

    public ResponseEntity<Object> addEnrolledCourse(EnrolledCourseDto request) {
        try {
            log.info("Save new enrolled course: {}", request);

            log.info("Find user by user id");
            Optional<User> user = userRepository.findById(request.getUserId());
            if(user.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

            log.info("Find course by course id");
            Optional<Course> course = courseRepository.searchCourseById(request.getCourseId());
            if(user.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

            EnrolledCourse enrolledCourse = EnrolledCourse.builder()
                .user(user.get())
                .course(course.get())
                .build();
            enrolledCourse = enrolledCourseRepository.save(enrolledCourse);

            log.info("Add report");
            reportService.addReport(enrolledCourse);

            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, enrolledCourse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing create new enrolled course, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllEnrolledCourse() {
        try {
            log.info("Get all enrolled course");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, enrolledCourseRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by get all enrolled course, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getEnrolledCourseDetail(Long id) {
        try {
            log.info("Find request detail by enrolled course id: {}", id);
            Optional<EnrolledCourse> enrolledCourseDetail = enrolledCourseRepository.findById(id);
            if (enrolledCourseDetail.isEmpty()) {
                log.info("enrolled course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, enrolledCourseDetail.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing get enrolled course by id, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteEnrolledCourse(Long id) {
        try {
            log.info("Executing delete enrolled course by id: {}", id);
            enrolledCourseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Data not found. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateRatingReview(String email, EnrolledCourseDto request) {
        try {
            log.info("Update rating review: {}", request);
            Optional<EnrolledCourse> enrolledCourse = enrolledCourseRepository.findById(request.getId());
            if (enrolledCourse.isEmpty()) {
                log.info("enrolled course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            log.info("Check user with the enroll course");
            if(!enrolledCourse.get().getUser().getUsername().equals(email)) {
                log.info("enrolled course id with email " + email + " not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            log.info("Update Rating and Review in enrolled course");
            enrolledCourse.get().setReview(request.getReview());
            // Double totalRating = courseRepository.getTotalRating(enrolledCourse.)
            enrolledCourse.get().setRating(request.getRating());
            enrolledCourseRepository.save(enrolledCourse.get());

            log.info("Update rating in course");

            log.info("Get course by id");
            Optional<Course> course = courseRepository.findById(enrolledCourse.get().getCourse().getId());
            if (course.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            log.info("Save rating in course");
            Integer ratingEnrolledCourse = enrolledCourseRepository.countRatingByCourse(course.get().getId());

            if(ratingEnrolledCourse > 1) {
                Double sumRating = enrolledCourseRepository.sumRatingByCourse(course.get().getId());
                Double ratingCourse = sumRating/ratingEnrolledCourse;
                course.get().setTotalRating(ratingCourse);
                courseRepository.save(course.get());
            } else {
                course.get().setTotalRating(request.getRating());
                courseRepository.save(course.get());
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, enrolledCourse.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing update rating review, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getEnrolledCourseByUser(String email) {
        try {
            log.info("Find user: {}", email);

            User user = userRepository.findUsername(email);
            List<EnrolledCourse> enrolledCourses = enrolledCourseRepository.getEnrolledCourseByUser(user.getId());

            if (enrolledCourses.isEmpty()) {
                log.info("enrolled courses empty");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, enrolledCourses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing get enrolled course by user, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getEnrolledCourseByCourse(Long id) {
        try {
            log.info("Find course by course id");
            Optional<Course> course = courseRepository.findById(id);
            
            if(course.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            
            List<EnrolledCourse> enrolledCourses = enrolledCourseRepository.getEnrolledCourseByCourse(id);
            if (enrolledCourses.isEmpty()) {
                log.info("enrolled courses empty");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, enrolledCourses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing get enrolled course by course, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateProgress(Long id, EnrolledCourseDto request) {
        try {
            check = false; 
            totalMaterials = 0;
            checkAdmin = false;

            log.info("Get enrolled course by id");
            Optional<EnrolledCourse> enrolledCourse = enrolledCourseRepository.findById(id);
            if (enrolledCourse.isEmpty()) {
                log.info("enrolled course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            log.info("Check user with enrolled course user {}", request.getEmail());
            User user = userRepository.findUsername(request.getEmail());
            user.getRoles().forEach(checkRole -> {
                if(checkRole.getName().equals(RoleEnum.ROLE_ADMIN)) {
                    checkAdmin = true;
                }
            });

            if(checkAdmin == false) {
                if(!enrolledCourse.get().getUser().getUsername().equals(request.getEmail()))
                    throw new Exception("Course taken id " + id + " with email " + request.getEmail() + " not found");
            }

            log.info("Get section course by enrolled course");
            List<Section> sections = sectionRepository.searchAllSections(enrolledCourse.get().getCourse().getId());

            sections.forEach(checkSection -> {
                List<Material> materials = checkSection.getMaterials();
                materials.forEach(checkMaterial -> {
                    if(checkMaterial.getId().equals(request.getMaterialId())) {
                        material = checkMaterial;
                        check = true;
                    }
                    if(checkMaterial.getIsDeleted() == false) totalMaterials++;
                });
            });

            if(!check || material.getIsDeleted() == true)
                throw new Exception("material id " + request.getMaterialId() + " not found");

            log.info("Update report");
            Report report = reportRepository.getReport(id, request.getMaterialId());

            if(checkAdmin == false) {
                report.setIsCompleted(true);
            } else {
                if(report.getIsCompleted() == true) {
                    log.info("progress completed");
                } else {
                    throw new Exception("Progress is not completed");
                }
            }

            reportRepository.save(report);

            Integer completedCourse = reportRepository.findByCompleted(id).size();
            Integer progress = (completedCourse*100)/totalMaterials;

            enrolledCourse.get().setProgress(progress);

            enrolledCourseRepository.save(enrolledCourse.get());
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, enrolledCourse.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by updating report enrolled course, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
}
