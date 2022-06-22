package com.projectlms.projectlms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectlms.projectlms.domain.dto.MaterialDto;
import com.projectlms.projectlms.service.MaterialService;

@RestController
@RequestMapping(value = "/course/{cid}/section/{sid}/material")
@CrossOrigin(origins = "https://edutiv-springboot.herokuapp.com")

public class MaterialController {
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> addMaterial(@PathVariable(value = "sid") Long sectionId, @RequestBody MaterialDto request) {
        request.setSectionId(sectionId);
        return materialService.addMaterial(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllMaterial(@PathVariable(value = "cid") Long courseId, @PathVariable(value = "sid") Long sectionId) {
        return materialService.getAllMaterial(courseId, sectionId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getMaterialDetail(@PathVariable(value = "cid") Long courseId, @PathVariable(value = "sid") Long sectionId, @PathVariable(value = "id") Long id) {
        return materialService.getMaterialDetail(courseId, sectionId, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteMaterial(@PathVariable(value = "id") Long id) {
        return materialService.deleteMaterial(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateMaterial(@PathVariable(value = "sid") Long sectionId, @PathVariable(value = "id") Long id, @RequestBody MaterialDto request) {
        request.setSectionId(sectionId);
        return materialService.updateMaterial(request, id);
    }
}
