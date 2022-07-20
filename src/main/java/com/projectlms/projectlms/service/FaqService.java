package com.projectlms.projectlms.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Faq;
import com.projectlms.projectlms.domain.dto.FaqDto;
import com.projectlms.projectlms.repository.FaqRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;

    public ResponseEntity<Object> addFaq(FaqDto request) {
        log.info("Save new faq: {}", request);
        Faq faq = Faq.builder()
            .question(request.getQuestion())
            .answer(request.getAnswer())
            .build();
        try {
            faq = faqRepository.save(faq);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, faq, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllFaq() {
        try {
            log.info("Get all faq");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, faqRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by get all faq, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getFaqDetail (Long id) {
        try {
            log.info("Find faq detail by faq id: {}", id);
            Optional<Faq> faqDetail = faqRepository.findById(id);
            if (faqDetail.isEmpty()) {
                log.info("faq not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, faqDetail.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing get faq by id, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteFaq(Long id) {
        try {
            log.info("Executing delete faq by id: {}", id);
            Optional<Faq> faqDetail = faqRepository.findById(id);
            if (faqDetail.isEmpty()) {
                log.info("faq not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            faqRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Get an error by delete faq. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }
}
