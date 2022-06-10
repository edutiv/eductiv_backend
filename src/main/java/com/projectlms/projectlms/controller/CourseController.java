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

import com.projectlms.projectlms.domain.dto.CourseDto;
import com.projectlms.projectlms.service.CourseService;

@RestController
@RequestMapping(value = "/course")
public class CourseController {
    
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> addCourse(@RequestBody CourseDto request) {
        return courseService.addCourse(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllCourse() {
        return courseService.getAllCourse();
    }

    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<Object> getCouresDetail(@PathVariable(value = "id") Long id) {
        return courseService.getCouresDetail(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "id") Long id) {
        return courseService.deleteCourse(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "id") Long id, @RequestBody CourseDto request) {
        return courseService.updateCourse(request, id);
    }

}
