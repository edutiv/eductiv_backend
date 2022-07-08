package com.projectlms.projectlms.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.domain.dao.RequestForm;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.domain.dto.RequestFormDto;
import com.projectlms.projectlms.repository.CategoryRepository;
import com.projectlms.projectlms.repository.RequestRepository;
import com.projectlms.projectlms.repository.UserRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import com.projectlms.projectlms.constant.AppConstant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<Object> addRequest(RequestFormDto req) {
        try{
            log.info("Save new request: {}", req);

            log.info("Find specialization by category id");
            Optional<Category> category = categoryRepository.searchCategoryById(req.getCategoryId());
            if(category.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

            log.info("Find user by user id");
            Optional<User> user = userRepository.findById(req.getUserId());
            if(user.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

            RequestForm request = RequestForm.builder()
                .user(user.get())
                .title(req.getRequestType())
                .category(category.get())
                .requestType(req.getRequestType())
                .build();
                request = requestRepository.save(request);

            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, request, HttpStatus.OK);
        }catch(Exception e) {
            log.error("Get an error by executing create new request, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllRequest() {
        try {
            log.info("Get all request user");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, requestRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by get all request user, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getRequestDetail(Long id) {
        try {
            log.info("Find request user detail by id: {}", id);
            Optional<RequestForm> request = requestRepository.findById(id);
            if (request.isEmpty()) {
                log.info("request not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, request.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by executing get request by id, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
