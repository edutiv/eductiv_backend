package com.projectlms.projectlms.service;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.domain.dao.RoleEnum;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.domain.dto.UserDto;
import com.projectlms.projectlms.repository.CategoryRepository;
import com.projectlms.projectlms.repository.UserRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private Boolean check;

    public ResponseEntity<Object> getAllUser() {
        log.info("Get all users");
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, userRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Object> getUserDetail(Long id) {
        log.info("Find user detail by user id: {}", id);
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user.get(), HttpStatus.OK);
    }

    public ResponseEntity<Object> getUserDetail(String email) {
        log.info("Find user detail by user id: {}", email);
        Optional<User> user = userRepository.findByUsername(email);
        if(user.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user.get(), HttpStatus.OK);
    }

    public ResponseEntity<Object> updateUserbyUser(UserDto request) {
        try {
            log.info("Update user: {}", request);
            Optional<User> user = userRepository.findByUsername(request.getEmail());
            if (user.isEmpty()) {
                log.info("user not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Find specialization by category id");
            Optional<Category> category = categoryRepository.searchCategoryById(request.getSpecializationId());
            if(category.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            
            //user.get().setProfileImage(request.getProfileImage());
            user.get().setCategory(category.get());
            userRepository.save(user.get());
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by update category, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateUserbyAdmin(Long id, UserDto request) {
        try {
            log.info("Update user: {}", request);
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                log.info("user not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            log.info("Find specialization by category id");
            Optional<Category> category = categoryRepository.searchCategoryById(request.getSpecializationId());
            if(category.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

            user.get().setFirstname(request.getFirstname());
            user.get().setLastname(request.getLastname());
            user.get().setUsername(request.getEmail());
            user.get().setCategory(category.get());
            
            userRepository.save(user.get());
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Get an error by update category, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> changePassword(UserDto request) {
        try {
            log.info("Find user: {}", request);
            Optional<User> user = userRepository.findByUsername(request.getEmail());
            if (user.isEmpty()) {
                log.info("user not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }

            log.info("Check password");
            Boolean isMatch = passwordEncoder.matches(request.getCurrentPassword(), user.get().getPassword());
            if(!isMatch) throw new Exception("Password does not match");

            log.info("Save new password");
            user.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
            
            userRepository.save(user.get());
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user.get(), HttpStatus.OK);
        } catch(Exception e) {
            log.error("Change password error, Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        try {
            log.info("Check user is not admin");
            check=false;
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                log.info("user not found");
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            user.get().getRoles().forEach(role -> {
                if(role.getName().equals(RoleEnum.ROLE_ADMIN)) check = true;
            });
            if(check == true) {
                log.info("can't delete admin");
                return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("Executing delete user by id: {}", id);
            userRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Getting error delete user: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }
}