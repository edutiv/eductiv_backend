package com.projectlms.projectlms.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.domain.dao.Role;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.domain.dto.UserDto;
import com.projectlms.projectlms.repository.CategoryRepository;
import com.projectlms.projectlms.repository.RoleRepository;
import com.projectlms.projectlms.repository.UserRepository;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository,CategoryRepository categoryRepository, PasswordEncoder passwordEncoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    // public ResponseEntity<Object> addUser(UserDto request) {
    //     log.info("Save new user: {}", request);

    //     log.info("Find specialization by category id");
    //         Optional<Category> category = categoryRepository.searchCategoryById(request.getSpecializationId());
    //         if(category.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

    //     User user = User.builder()
    //         .firstname(request.getFirstname())
    //         .lastname(request.getLastname())
    //         //.email(request.getEmail())
    //         .username(request.getEmail())
    //         .password(request.getPassword())
    //         .category(category.get())
    //         .build();
    //     try {
    //         user = userRepository.save(user);
    //         return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user, HttpStatus.OK);
    //     } catch (Exception e) {
    //         return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

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
            // UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // String email = userDetails.getUsername();
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

    public ResponseEntity<Object> updateUserbyAdmin(UserDto request, Long id) {
        try {
            log.info("Update user: {}", request);
            // UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // String email = userDetails.getUsername();
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
            // user.get().setPassword(passwordEncoder.encode(request.getPassword()));
            //user.get().setProfileImage(request.getProfileImage());
            // Set<Role> roles = new HashSet<>();
            // request.getRoles().forEach(inputRole -> {
            //     Role role = roleRepository.findByName(inputRole)
            //         .orElseThrow(() -> new RuntimeException("ROLE NOT FOUND"));
            //     roles.add(role);
            // });
            // user.get().setRoles(roles);
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
            log.info("Executing delete user by id: {}", id);
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Data not found. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
        }
        return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, null, HttpStatus.OK);
    }
}

// public ResponseEntity<Object> updateUser(UserDto request, Long id) {
    //     try {
    //         log.info("Update user: {}", request);
    //         Optional<User> user = userRepository.findById(id);
    //         if (user.isEmpty()) {
    //             log.info("user not found");
    //             return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
    //         }
    //         log.info("Find specialization by category id");
    //         Optional<Category> category = categoryRepository.searchCategoryById(request.getSpecializationId());
    //         if(category.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);

    //         user.get().setFirstname(request.getFirstname());
    //         user.get().setLastname(request.getLastname());
    //         user.get().setUsername(request.getUsername());
    //         user.get().setPassword(request.getPassword());
    //         user.get().setCategory(category.get());
    //         userRepository.save(user.get());
    //         return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user.get(), HttpStatus.OK);
    //     } catch (Exception e) {
    //         log.error("Get an error by update category, Error : {}",e.getMessage());
    //         return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    // public ResponseEntity<Object> updateUserbyUser(UserDto request, Long id) {
    //     try {
    //         log.info("Update user: {}", request);
    //         // UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //         // String email = userDetails.getUsername();
    //         Optional<User> user = userRepository.findById(id);
    //         if (user.isEmpty()) {
    //             log.info("user not found");
    //             return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
    //         }
    //         log.info("Find specialization by category id");
    //         Optional<Category> category = categoryRepository.searchCategoryById(request.getSpecializationId());
    //         if(category.isEmpty()) return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            
    //         //user.get().setProfileImage(request.getProfileImage());
    //         user.get().setCategory(category.get());
    //         userRepository.save(user.get());
    //         return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user.get(), HttpStatus.OK);
    //     } catch (Exception e) {
    //         log.error("Get an error by update category, Error : {}",e.getMessage());
    //         return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
