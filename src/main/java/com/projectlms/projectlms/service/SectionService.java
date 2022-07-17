package com.projectlms.projectlms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dao.EnrolledCourse;
import com.projectlms.projectlms.domain.dao.Section;
import com.projectlms.projectlms.domain.dto.SectionDto;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.EnrolledCourseRepository;
import com.projectlms.projectlms.repository.MaterialRepository;
import com.projectlms.projectlms.repository.ReportRepository;
import com.projectlms.projectlms.repository.SectionRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SectionService {
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final MaterialRepository materialRepository;
    private final ReportRepository reportRepository;
    private final EnrolledCourseRepository enrolledCourseRepository;

    public ResponseEntity<Object> addSection(SectionDto request) {
        try {
            log.info("Save new section: {}", request);

            log.info("Find course by course id");
            Optional<Course> course = courseRepository.searchCourseById(request.getCourseId());
            if(course.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

            Section section = Section.builder()
                .course(course.get())
                .sectionName(request.getSectionName())
                .build();

            section = sectionRepository.save(section);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, section, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing create new section, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllSection(Long courseId) {
        try {
            log.info("Find course detail by course id: {}", courseId);
            Optional<Course> courseDetail = courseRepository.searchCourseById(courseId);
            if (courseDetail.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Get all section");
            List<Section> sections = sectionRepository.searchAllSections(courseId);
            if (sections.isEmpty()) {
                log.info("sections is empty");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, sections, HttpStatus.OK);
        
        } catch (Exception e) {
            log.error("Get an error by get all sections, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getSectionDetail(Long courseId, Long id) {
        try {
            log.info("Find course detail by course id: {}", courseId);
            Optional<Course> courseDetail = courseRepository.searchCourseById(courseId);
            if (courseDetail.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            log.info("Find section detail by id: {}", id);
            Optional<Section> sectionDetail = sectionRepository.searchSectionById(id, courseId);
            if (sectionDetail.isEmpty()) {
                log.info("section not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, sectionDetail.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing get section by id, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateSection(Long id, SectionDto request) {
        try {
            log.info("Find course by course id");
            Optional<Course> course = courseRepository.searchCourseById(request.getCourseId());
            if(course.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            log.info("Find section by section id");
            Optional<Section> section = sectionRepository.searchSectionById(id, request.getCourseId());
            if (section.isEmpty()) {
                log.info("section not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Update section: {}", request);
            section.get().setCourse(course.get());
            section.get().setSectionName(request.getSectionName());
            sectionRepository.save(section.get());
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, section.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by update section, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteSection(Long courseId, Long id) {
        try {
            log.info("Find course detail by course id: {}", courseId);
            Optional<Course> courseDetail = courseRepository.searchCourseById(courseId);
            if (courseDetail.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Find section by section id");
            Optional<Section> section = sectionRepository.searchSectionById(id, courseId);
            if (section.isEmpty()) {
                log.info("section not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            log.info("Executing delete section by id: {}", id);
            sectionRepository.deleteById(id);
            materialRepository.deleteMaterialBySection(id);

            log.info("update material progress");
            List<EnrolledCourse> enrolledCourses = enrolledCourseRepository.getEnrolledCourseByCourse(courseId);

            Integer totalMaterials = materialRepository.countMaterials(courseId).size();

            if(totalMaterials != 0) {
                enrolledCourses.forEach(enrolledCourse -> {
                    log.info("Update progress enrolled course {}", totalMaterials);
                    Integer completedCourse = reportRepository.findByCompleted(enrolledCourse.getId()).size();
                    Integer progress = (completedCourse*100)/totalMaterials;
                    enrolledCourse.setProgress(progress);
                    enrolledCourseRepository.save(enrolledCourse);
                });
            } else {
                enrolledCourses.forEach(enrolledCourse -> {
                    log.info("Update progress enrolled course total material 0");
                    enrolledCourse.setProgress(0);
                    enrolledCourseRepository.save(enrolledCourse);
                });
            }

        } catch (EmptyResultDataAccessException e) {
            log.error("Data not found. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }
}