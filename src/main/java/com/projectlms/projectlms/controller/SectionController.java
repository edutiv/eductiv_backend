package com.projectlms.projectlms.controller;

import org.springframework.http.ResponseEntity;
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
@RequestMapping(value = "/section")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> addSection(@RequestBody SectionDto request) {
        return sectionService.addSection(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllSection() {
        return sectionService.getAllSection();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getSectionDetail(@PathVariable(value = "id") Long id) {
        return sectionService.getSectionDetail(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSection(@PathVariable(value = "id") Long id) {
        return sectionService.deleteSection(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateSection(@PathVariable(value = "id") Long id, @RequestBody SectionDto request) {
        return sectionService.updateSection(request, id);
    }
}
