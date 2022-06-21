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

import com.projectlms.projectlms.domain.dto.CategoryDto;
import com.projectlms.projectlms.service.CategoryService;

@RestController
@RequestMapping(value = "/category")
@CrossOrigin(origins = "https://edutiv-springboot.herokuapp.com")
public class CategoryController {
    
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> addCategory(@RequestBody CategoryDto request) {
        return categoryService.addCategory(request);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getCategoryDetail(@PathVariable(value = "id") Long id) {
        return categoryService.getCategoryDetail(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable(value = "id") Long id) {
        return categoryService.deleteCategory(id);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable(value = "id") Long id, @RequestBody CategoryDto request) {
        return categoryService.updateCategory(request, id);
    }
}
