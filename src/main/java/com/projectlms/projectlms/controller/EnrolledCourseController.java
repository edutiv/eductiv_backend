package com.projectlms.projectlms.controller;

import java.security.Principal;

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

import com.projectlms.projectlms.domain.dto.EnrolledCourseDto;
import com.projectlms.projectlms.service.EnrolledCourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/enrolled")
public class EnrolledCourseController {
    private final EnrolledCourseService requestService;

    @PostMapping(value = "")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> addEnrolledCourse(Principal principal, @RequestBody EnrolledCourseDto request) {
        request.setEmail(principal.getName());
        return requestService.addEnrolledCourse(request);
    }

    @GetMapping(value = "")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllEnrolledCourset() {
        return requestService.getAllEnrolledCourse();
    }

    @GetMapping(value = "/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getEnrolledCourseDetail(@PathVariable(value = "id") Long id) {
        return requestService.getEnrolledCourseDetail(id);
    }

    @PutMapping(value = "/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateRatingReview(Principal principal, @PathVariable(value = "id") Long id, @RequestBody EnrolledCourseDto request) {
        request.setId(id);
        return requestService.updateRatingReview(principal.getName(), request);
    }

    @DeleteMapping(value = "/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteEnrolledCourse(@PathVariable(value = "id") Long id) {
        return requestService.deleteEnrolledCourse(id);
    }

    @GetMapping(value = "/history")
    public ResponseEntity<Object> getEnrolledCourseByUser(Principal principal) {
        return requestService.getEnrolledCourseByUser(principal.getName());
    }

    @GetMapping(value = "/courses/{id}")
    public ResponseEntity<Object> getEnrolledCourseByCourse(@PathVariable Long id) {
        return requestService.getEnrolledCourseByCourse(id);
    }

    @PutMapping(value = "/progress/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> updateProgress(Principal principal, @PathVariable Long id, @RequestBody EnrolledCourseDto request) {
        request.setEmail(principal.getName());
        return requestService.updateProgress(id, request);
    }
}
