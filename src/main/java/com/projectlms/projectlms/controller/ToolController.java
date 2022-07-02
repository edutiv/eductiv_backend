package com.projectlms.projectlms.controller;

import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectlms.projectlms.domain.dto.ToolDto;
import com.projectlms.projectlms.service.ToolService;

@RestController
@RequestMapping(value = "/course/{cid}/tool")
@CrossOrigin(origins = "https://edutiv-springboot.herokuapp.com")

public class ToolController {
    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @PostMapping(value = "")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> addTool(@PathVariable(value = "cid") Long courseId, @RequestBody ToolDto request) {
        request.setCourseId(courseId);
        return toolService.addTool(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllTool(@PathVariable(value = "cid") Long courseId) {
        return toolService.getAllTool(courseId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getToolDetail(@PathVariable(value = "cid") Long courseId, @PathVariable(value = "id") Long id) {
        return toolService.getToolDetail(courseId, id);
    }

    @DeleteMapping(value = "/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteTool(@PathVariable(value = "cid") Long courseId, @PathVariable(value = "id") Long id) {
        return toolService.deleteTool(courseId, id);
    }

    @PutMapping(value = "/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateTool(@PathVariable(value = "cid") Long courseId, @PathVariable(value = "id") Long id, @RequestBody ToolDto request) {
        request.setCourseId(courseId);
        return toolService.updateTool(request, id);
    }
}
