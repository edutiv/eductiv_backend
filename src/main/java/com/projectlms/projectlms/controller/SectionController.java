package com.projectlms.projectlms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectlms.projectlms.domain.dto.SectionDto;
import com.projectlms.projectlms.service.SectionService;

@RestController
@RequestMapping(value = "/course/{cid}/section")

public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping(value = "")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENTOR')")
    public ResponseEntity<Object> addSection(@PathVariable(value = "cid") Long courseId, @RequestBody SectionDto request) {
        request.setCourseId(courseId);
        return sectionService.addSection(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllSection(@PathVariable(value = "cid") Long courseId) {
        return sectionService.getAllSection(courseId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getSectionDetail(@PathVariable(value = "cid") Long courseId, @PathVariable(value = "id") Long id) {
        return sectionService.getSectionDetail(courseId, id);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENTOR')")
    public ResponseEntity<Object> deleteSection(@PathVariable(value = "cid") Long courseId, @PathVariable(value = "id") Long id) {
        return sectionService.deleteSection(courseId, id);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENTOR')")
    public ResponseEntity<Object> updateSection(@PathVariable (value = "cid") Long courseId, @PathVariable(value = "id") Long id, @RequestBody SectionDto request) {
        request.setCourseId(courseId);
        return sectionService.updateSection(id, request);
    }
}
