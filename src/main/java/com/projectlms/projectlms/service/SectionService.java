package com.projectlms.projectlms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dao.Section;
import com.projectlms.projectlms.domain.dto.SectionDto;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.SectionRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository, CourseRepository courseRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
    }

    public ResponseEntity<Object> addSection(SectionDto request) {
        try {
            log.info("Save new section: {}", request);

            log.info("Find course by course id");
            Optional<Course> course = courseRepository.findOne(request.getCourseId());
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

    public ResponseEntity<Object> getAllSection() {
        try {
            log.info("Get all section");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, sectionRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by get all sections, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getSectionDetail(Long id) {
        try {
            log.info("Find section detail by section id: {}", id);
            Optional<Section> sectionDetail = sectionRepository.findOne(id);
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

    public ResponseEntity<Object> updateSection(SectionDto request, Long id) {
        try {
            log.info("Update section: {}", request);
            Optional<Section> section = sectionRepository.findOne(id);
            if (section.isEmpty()) {
                log.info("section not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Find course by course id");
            Optional<Course> course = courseRepository.findOne(request.getCourseId());
            if(course.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            section.get().setCourse(course.get());
            section.get().setSectionName(request.getSectionName());
            sectionRepository.save(section.get());
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, section.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by update section, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteSection(Long id) {
        try {
            log.info("Executing delete section by id: {}", id);
            sectionRepository.delete(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Data not found. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }
}
