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
import com.projectlms.projectlms.domain.dao.Tool;
import com.projectlms.projectlms.domain.dto.ToolDto;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.ToolRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ToolService {
    private final ToolRepository toolRepository;
    private final CourseRepository courseRepository;

    public ResponseEntity<Object> addTool(ToolDto request) {
        try {
            log.info("Save new tool: {}", request);

            log.info("Find course by course id");
            Optional<Course> course = courseRepository.searchCourseById(request.getCourseId());
            if(course.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

            Tool tool = Tool.builder()
                .toolName(request.getToolName())
                .toolIcon(request.getToolIcon())
                .toolUrl(request.getToolUrl())
                .course(course.get())
                .build();

            tool = toolRepository.save(tool);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, tool, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing create new tool, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllTool(Long courseId) {
        try {
            log.info("Find course detail by course id: {}", courseId);
            Optional<Course> courseDetail = courseRepository.searchCourseById(courseId);
            if (courseDetail.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Get all tools");
            List<Tool> tools = toolRepository.searchAllTools(courseId);
            if (tools.isEmpty()) {
                log.info("tools is empty");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, tools, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by get all tools, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getToolDetail(Long courseId, Long id) {
        try {
            log.info("Find course detail by course id: {}", courseId);
            Optional<Course> courseDetail = courseRepository.searchCourseById(courseId);
            if (courseDetail.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            log.info("Find tool detail by id: {}", id);
            Optional<Tool> toolDetail = toolRepository.searchToolById(id, courseId);
            if (toolDetail.isEmpty()) {
                log.info("tool not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, toolDetail.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing get tool by id, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateTool(Long id, ToolDto request) {
        try {

            log.info("Find course by course id");
            Optional<Course> course = courseRepository.searchCourseById(request.getCourseId());
            if(course.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            log.info("Update tool: {}", request);
            Optional<Tool> tool = toolRepository.searchToolById(id, request.getCourseId());
            if (tool.isEmpty()) {
                log.info("tool not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            
            tool.get().setToolName(request.getToolName());
            tool.get().setToolIcon(request.getToolIcon());
            tool.get().setToolUrl(request.getToolUrl());
            tool.get().setCourse(course.get());
            toolRepository.save(tool.get());
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, tool.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by update tool, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteTool(Long courseId, Long id) {
        try {
            log.info("Find course detail by course id: {}", courseId);
            Optional<Course> courseDetail = courseRepository.searchCourseById(courseId);
            if (courseDetail.isEmpty()) {
                log.info("course not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Find tool by tool id");
            Optional<Tool> tool = toolRepository.searchToolById(id, courseId);
            if (tool.isEmpty()) {
                log.info("tool not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Executing delete tool by id: {}", id);
            toolRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Data not found. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }
}