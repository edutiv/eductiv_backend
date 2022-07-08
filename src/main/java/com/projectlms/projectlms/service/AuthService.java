package com.projectlms.projectlms.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.domain.dao.Role;
import com.projectlms.projectlms.domain.dao.RoleEnum;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.domain.dto.UserDto;
import com.projectlms.projectlms.repository.CategoryRepository;
import com.projectlms.projectlms.repository.RoleRepository;
import com.projectlms.projectlms.repository.UserRepository;
import com.projectlms.projectlms.response.TokenResponse;
import com.projectlms.projectlms.security.JwtProvider;
import com.projectlms.projectlms.util.ResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final AuthenticationManager authenticationManager; 
    private final JwtProvider jwtTokenProvider; 
    private final PasswordEncoder passwordEncoder;
    
    public UserDto register(UserDto req) {
        try {
            log.info("Search username in database");
            if (userRepository.findUsername(req.getEmail()) != null) {
                throw new Exception("User with email " + req.getEmail() + " is already exist");
            } 
            
            User user = new User();
            user.setFirstname(req.getFirstname());
            user.setLastname(req.getLastname());
            user.setUsername(req.getEmail());
            user.setPassword(passwordEncoder.encode(req.getPassword()));

            if(req.getSpecializationId() != null) {
                Category category = categoryRepository.findById(req.getSpecializationId())
                    .orElseThrow(() -> new RuntimeException("Specialization not found"));
                user.setCategory(category);
            }
            Set<Role> roles = new HashSet<>();
                if(req.getRoles() == null) {
                    Role role = roleRepository.findByName(RoleEnum.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("ROLE NOT FOUND"));
                    
                    roles.add(role);
                }else {
                    req.getRoles().forEach(inputRole -> {
                        Role role = roleRepository.findByName(inputRole)
                            .orElseThrow(() -> new RuntimeException("ROLE NOT FOUND"));
                        roles.add(role);
                    });
                }
                user.setRoles(roles);
            userRepository.save(user);

            req.setPassword("*".repeat(req.getPassword().length()));
            log.info("User {} saved", req.getEmail());
            return req;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    
    }

    public TokenResponse generateToken(UserDto req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(),req.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);

            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwt);
            log.info("Token created");
            
            return tokenResponse;
            //return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, tokenResponse.getToken(), HttpStatus.CREATED);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // public ResponseEntity<Object> updateUser(UserDto userDto) {
    //     UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //     String email = userDetails.getUsername();
    //     Optional<User> user = userRepository.findByUsername(email);
    //     //User user = new User();

    //     if (!Objects.equals(userDto.getFirstname(), "") && userDto.getFirstname() != null) {
    //         user.get().setFirstname(userDto.getFirstname());
    //     }
        
    //     if (!Objects.equals(userDto.getLastname(), "") && userDto.getLastname() != null) {
    //         user.get().setLastname(userDto.getLastname());
    //     }

    //     if (!Objects.equals(userDto.getEmail(), "") && userDto.getEmail() != null) {
    //         // if (Boolean.TRUE.equals(userRepository.existsByUsername(userDto.getUsername()))) {
    //         //     return Response.build(Response.exist("user", "username", userDto.getUsername()), null, null, HttpStatus.BAD_REQUEST);
    //         // }
    //         user.get().setUsername(userDto.getEmail());
    //     }

    //     if (!Objects.equals(userDto.getPassword(), "") && userDto.getPassword() != null) {
    //         user.get().setPassword(passwordEncoder.encode(userDto.getPassword()));
    //     }

    //     Category category = categoryRepository.findById(userDto.getSpecializationId())
    //             .orElseThrow(() -> new RuntimeException("Specialization not found"));
    //     user.get().setCategory(category);

    //     Set<Role> roles = new HashSet<>();
    //         userDto.getRoles().forEach(inputRole -> {
    //             Role role = roleRepository.findByName(inputRole)
    //                 .orElseThrow(() -> new RuntimeException("ROLE NOT FOUND"));
    //             roles.add(role);
    //         });
    //     user.get().setRoles(roles);

    //     userRepository.save(user.get());

    //     userDto.setPassword("*".repeat(userDto.getPassword().length()));
    //     log.info("User {} saved", userDto.getEmail());
        
    //     return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, user.get(), HttpStatus.OK);
    // }

}
