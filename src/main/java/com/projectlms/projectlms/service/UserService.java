package com.projectlms.projectlms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.domain.dto.UserDto;
import com.projectlms.projectlms.repository.UserRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> save(UserDto request) {
        log.info("Save new user: {}", request);
        User user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(request.getPassword())
            .role(request.getRole())
            .build();
        try {
            user = userRepository.save(user);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllUser() {
        log.info("Get all users");
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, userRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Object> getUserDetail(Long id) {
        log.info("Find user detail by user id: {}", id);
        Optional<User> user = userRepository.findOne(id);
        if(user.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user.get(), HttpStatus.OK);
    }
}
