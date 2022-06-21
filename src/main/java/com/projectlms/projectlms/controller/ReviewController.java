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

import com.projectlms.projectlms.domain.dto.ReviewDto;
import com.projectlms.projectlms.service.ReviewService;

@RestController
@RequestMapping(value = "/course/{cid}/review")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> addReview(@PathVariable(value = "cid") Long courseId, @RequestBody ReviewDto request) {
        request.setCourseId(courseId);
        return reviewService.addReview(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllReview() {
        return reviewService.getAllReview();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getReviewDetail(@PathVariable(value = "id") Long id) {
        return reviewService.getReviewDetail(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable(value = "id") Long id) {
        return reviewService.deleteReview(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateReview(@PathVariable(value = "cid") Long courseId, @PathVariable(value = "id") Long id, @RequestBody ReviewDto request) {
        request.setCourseId(courseId);
        return reviewService.updateReview(request, id);
    }
    
}
