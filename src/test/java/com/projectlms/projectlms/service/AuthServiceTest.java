package com.projectlms.projectlms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

@SpringBootTest(classes = AuthService.class)
public class AuthServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private User user;
    private UserDto userDto;
    private List<User> users;
    private Category category;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = EASY_RANDOM.nextObject(User.class);
        userDto = EASY_RANDOM.nextObject(UserDto.class);
        users = EASY_RANDOM.objects(User.class, 2).collect(Collectors.toList());
        category = EASY_RANDOM.nextObject(Category.class);
    }

    @Test
    void registerUser_Success_Test() {
        User user = new User();

        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userDto.setRoles(null);
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(role));
        roles.add(role);
        user.setRoles(roles);

        userDto.setSpecializationId(category.getId());
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        user.setCategory(category);

        when(userRepository.save(user)).thenReturn(user);
        var result = authService.register(userDto);
        assertEquals(user, result);
    }

    @Test
    void registerAdmin_Success_Test() {
        User user = new User();

        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        List<RoleEnum> listRoles = new ArrayList<>();
        listRoles.add(RoleEnum.ROLE_ADMIN);
        userDto.setRoles(listRoles);

        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        userDto.getRoles().forEach(registerRole -> {
            when(roleRepository.findByName(registerRole))
                .thenReturn(Optional.of(role));
            roles.add(role);
        });

        user.setRoles(roles);
        userDto.setSpecializationId(null);
        var result = authService.register(userDto);
        assertEquals(user, result);
    }

    @Test
    void register_Exception_Test() {
        when(userRepository.findUsername(userDto.getEmail()))
            .thenReturn(user);
        
        assertThrows(RuntimeException.class, () -> {
            authService.register(userDto);
        });
    }

    @Test
    void register_Exception2_Test() {
        User testing = new User();

        testing.setFirstname(userDto.getFirstname());
        testing.setLastname(userDto.getLastname());
        testing.setUsername(userDto.getEmail());
        testing.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userDto.setRoles(null);

        assertThrows(RuntimeException.class, () -> {
            authService.register(userDto);
        });
    }

    @Test
    void register_Exception3_Test() {
        User testing = new User();

        testing.setFirstname(userDto.getFirstname());
        testing.setLastname(userDto.getLastname());
        testing.setUsername(userDto.getEmail());
        testing.setPassword(passwordEncoder.encode(userDto.getPassword()));

        List<RoleEnum> listRoles = new ArrayList<>();
        listRoles.add(RoleEnum.ROLE_ADMIN);
        userDto.setRoles(listRoles);

        userDto.getRoles().forEach(registerRole -> {
            assertThrows(RuntimeException.class, () -> {
                authService.register(userDto);
            });
        });
    }

    @Test
    void register_Exception4_Test() {
        User testing = new User();

        testing.setFirstname(userDto.getFirstname());
        testing.setLastname(userDto.getLastname());
        testing.setUsername(userDto.getEmail());
        testing.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userDto.setRoles(null);

        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(role));
        roles.add(role);

        testing.setRoles(roles);
        userDto.setSpecializationId(category.getId());

        assertThrows(RuntimeException.class, () -> {
            authService.register(userDto);
        });
    }

    @Test
    void login_Success_Test() {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        when(userRepository.findUsername(userDto.getEmail())).thenReturn(user);

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(jwt);

        var result = authService.generateToken(userDto);
        assertEquals(tokenResponse, result);

    }   
    
    // @Test
    // void login_Exception_Test() {
    //     UserDto userDto = new UserDto();
       
    //     assertThrows(RuntimeException.class, () -> {
    //         authService.generateToken(userDto);
    //     });
    // }     
}
