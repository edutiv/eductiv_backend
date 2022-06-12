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

import com.projectlms.projectlms.domain.dto.MaterialDto;
import com.projectlms.projectlms.service.MaterialService;

@RestController
@RequestMapping(value = "/material")
public class MaterialController {
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> addMaterial(@RequestBody MaterialDto request) {
        return materialService.addMaterial(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllMaterial() {
        return materialService.getAllMaterial();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getMaterialDetail(@PathVariable(value = "id") Long id) {
        return materialService.getMaterialDetail(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteMaterial(@PathVariable(value = "id") Long id) {
        return materialService.deleteMaterial(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateMaterial(@PathVariable(value = "id") Long id, @RequestBody MaterialDto request) {
        return materialService.updateMaterial(request, id);
    }
}
