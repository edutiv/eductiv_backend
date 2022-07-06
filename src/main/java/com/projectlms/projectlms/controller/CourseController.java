package com.projectlms.projectlms.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projectlms.projectlms.domain.dto.CourseDto;
import com.projectlms.projectlms.service.CourseService;

@RestController
@RequestMapping(value = "/course")
//@CrossOrigin(origins = "https://edutiv-springboot.herokuapp.com")
public class CourseController {
    
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping(value = "")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> addCourse(@RequestBody CourseDto request) {
        return courseService.addCourse(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllCourse() {
        return courseService.getAllCourse();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getCourseDetail(@PathVariable(value = "id") Long id) {
        return courseService.getCourseDetail(id);
    }

    @DeleteMapping(value = "/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "id") Long id) {
        return courseService.deleteCourse(id);
    }

    @PutMapping(value = "/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "id") Long id, @RequestBody CourseDto request) {
        return courseService.updateCourse(id, request);
    }

    @GetMapping(value = "/recommendations")
    public ResponseEntity<Object> getCourseByUserSpecialization(Principal principal) {
        return courseService.getCourseByUserSpecialization(principal.getName());
    }

    @GetMapping(value = "/search/{course_name}")
    public ResponseEntity<Object> searchByCourseName(@PathVariable(value = "course_name") String course_name) {
        return courseService.searchByCourseName(course_name);
    }

    // @GetMapping(value = "/search")
    // public ResponseEntity<Object> getCourseByCourseName(@RequestParam(value = "q") String course_name) {
    //     return courseService.getCourseByCourseName(course_name);
    // }


}
