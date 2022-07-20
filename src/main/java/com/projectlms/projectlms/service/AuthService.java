package com.projectlms.projectlms.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    
    public User register(UserDto req) {
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

            if(req.getSpecializationId() != null) {
                Category category = categoryRepository.findById(req.getSpecializationId())
                    .orElseThrow(() -> new RuntimeException("Specialization not found"));
                user.setCategory(category);
            }
            userRepository.save(user);

            req.setPassword("*".repeat(req.getPassword().length()));
            log.info("User {} saved", req.getEmail());
            return user;
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
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
