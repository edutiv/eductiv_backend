package com.projectlms.projectlms.service;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.domain.dto.CategoryDto;
import com.projectlms.projectlms.repository.CategoryRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public ResponseEntity<Object> addCategory(CategoryDto request) {
        log.info("Save new category: {}", request);
        Category category = Category.builder()
            .categoryName(request.getCategoryName())
            .categoryImage(request.getCategoryImage())
            .description(request.getDescription()) 
            .build();
        
        try {
            category = categoryRepository.save(category);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, category, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllCategories() {
        try {
            log.info("Get all categories");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, categoryRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by get all categories, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getCategoryDetail (Long id) {
        try {
            log.info("Find category detail by category id: {}", id);
            Optional<Category> categoryDetail = categoryRepository.findById(id);
            if (categoryDetail.isEmpty()) {
                log.info("category not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, categoryDetail.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing get category by id, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateCategory(CategoryDto request, Long id) {
        try {
            log.info("Update category: {}", request);
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()) {
                log.info("category not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            category.get().setCategoryName(request.getCategoryName());
            category.get().setCategoryImage(request.getCategoryImage());
            category.get().setDescription(request.getDescription());
            categoryRepository.save(category.get());
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, category.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by update category, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteCategory(Long id) {
        try {
            log.info("Executing delete category by id: {}", id);
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()) {
                log.info("faq not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Get an error by delete faq. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }
}
