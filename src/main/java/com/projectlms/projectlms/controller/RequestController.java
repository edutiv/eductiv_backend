package com.projectlms.projectlms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectlms.projectlms.domain.dto.RequestFormDto;
import com.projectlms.projectlms.service.RequestService;

@RestController
@RequestMapping(value = "/request")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping(value = "")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> addRequest(@RequestBody RequestFormDto request) {
        return requestService.addRequest(request);
    }

    @GetMapping(value = "")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllRequest() {
        return requestService.getAllRequest();
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getRequestDetail(@PathVariable(value = "id") Long id) {
        return requestService.getRequestDetail(id);
    }
    
}
