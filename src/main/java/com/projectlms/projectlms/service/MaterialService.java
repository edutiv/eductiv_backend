package com.projectlms.projectlms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Material;
import com.projectlms.projectlms.domain.dao.Section;
import com.projectlms.projectlms.domain.dto.MaterialDto;
import com.projectlms.projectlms.repository.MaterialRepository;
import com.projectlms.projectlms.repository.SectionRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final SectionRepository sectionRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository, SectionRepository sectionRepository) {
        this.materialRepository = materialRepository;
        this.sectionRepository = sectionRepository;
    }

    public ResponseEntity<Object> addMaterial(MaterialDto request) {
        try {
            log.info("Save new material: {}", request);

            log.info("Find section by section id");
            Optional<Section> section = sectionRepository.findOne(request.getSectionId());
            if(section.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

            Material material = Material.builder()
                .section(section.get())
                .materialType(request.getMaterialType())
                .materialName(request.getMaterialName())
                .materialUrl(request.getMaterialUrl())
                //.isCompleted(request.getIsComplete())
                .build();

            material = materialRepository.save(material);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, material, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing create new material, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllMaterial() {
        try {
            log.info("Get all material");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, materialRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by get all materials, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getMaterialDetail(Long id) {
        try {
            log.info("Find material detail by material id: {}", id);
            Optional<Material> materialDetail = materialRepository.findOne(id);
            if (materialDetail.isEmpty()) {
                log.info("material not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, materialDetail.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing get material by id, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateMaterial(MaterialDto request, Long id) {
        try {
            log.info("Update material: {}", request);
            Optional<Section> section = sectionRepository.findOne(request.getSectionId());
            if (section.isEmpty()) { 
                log.info("section not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Find material by material id");
            Optional<Material> material = materialRepository.findOne(id);
            if(material.isEmpty()) {
                log.info("material not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            material.get().setSection(section.get());
            material.get().setMaterialType(request.getMaterialType());
            material.get().setMaterialName(request.getMaterialName());
            material.get().setMaterialUrl(request.getMaterialUrl());
            material.get().setIsCompleted(request.getIsComplete());
            materialRepository.save(material.get());
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, material.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by update material, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteMaterial(Long id) {
        try {
            log.info("Executing delete material by id: {}", id);
            materialRepository.delete(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Data not found. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }
}
