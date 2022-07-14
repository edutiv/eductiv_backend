package com.projectlms.projectlms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CustomUserDetailsService.class)
public class CustomUserDetailsServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private User user;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = EASY_RANDOM.nextObject(User.class);
    }
    
    @Test
    void loadUserByUsername_Success_Test() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        var result = customUserDetailsService.loadUserByUsername(user.getUsername());
        assertEquals(CustomUserDetails.build(user), result);
    }

    @Test
    void loadUserByUsername_Exception_Test() {
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(user.getUsername());
        });
    }
}
