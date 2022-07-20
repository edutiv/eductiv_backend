package com.projectlms.projectlms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectlms.projectlms.domain.dto.FaqDto;
import com.projectlms.projectlms.service.FaqService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/faq")
public class FaqController {
    private final FaqService faqService;

    @PostMapping(value = "")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> addFaq(@RequestBody FaqDto request) {
        return faqService.addFaq(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllFaq() {
        return faqService.getAllFaq();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getFaqDetail(@PathVariable(value = "id") Long id) {
        return faqService.getFaqDetail(id);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteFaq(@PathVariable(value = "id") Long id) {
        return faqService.deleteFaq(id);
    }
}
