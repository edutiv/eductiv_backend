package com.projectlms.projectlms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dao.Tool;
import com.projectlms.projectlms.domain.dto.ToolDto;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.ToolRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ToolService {
    private final ToolRepository toolRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ToolService(ToolRepository toolRepository, CourseRepository courseRepository) {
        this.toolRepository = toolRepository;
        this.courseRepository = courseRepository;
    }

    public ResponseEntity<Object> addTool(ToolDto request) {
        try {
            log.info("Save new tool: {}", request);

            log.info("Find course by course id");
            Optional<Course> course = courseRepository.findOne(request.getCourseId());
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

    public ResponseEntity<Object> getAllTool() {
        try {
            log.info("Get all tools");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, toolRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by get all tools, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getToolDetail(Long id) {
        try {
            log.info("Find tool detail by tool id: {}", id);
            Optional<Tool> toolDetail = toolRepository.findOne(id);
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

    public ResponseEntity<Object> updateTool(ToolDto request, Long id) {
        try {
            log.info("Update tool: {}", request);
            Optional<Tool> tool = toolRepository.findOne(id);
            if (tool.isEmpty()) {
                log.info("tool not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Find course by course id");
            Optional<Course> course = courseRepository.findOne(request.getCourseId());
            if(course.isEmpty()) {
                log.info("course not found");
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

    public ResponseEntity<Object> deleteTool(Long id) {
        try {
            log.info("Executing delete tool by id: {}", id);
            toolRepository.delete(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Data not found. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }
}
