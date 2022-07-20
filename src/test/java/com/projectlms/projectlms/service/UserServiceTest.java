package com.projectlms.projectlms.service;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projectlms.projectlms.domain.common.ApiResponse;
import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.domain.dto.UserDto;
import com.projectlms.projectlms.repository.CategoryRepository;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.ToolRepository;
import com.projectlms.projectlms.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private User user;
    private UserDto userDto;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDto = EASY_RANDOM.nextObject(UserDto.class);
        user= EASY_RANDOM.nextObject(User.class);
    }

    @Test
    void getAllUser_Success_Test() {
        when(userRepository.findAll()).thenReturn(List.of(User.builder()
            .id(1L)
            .firstname("admin")
            .lastname("edutiv")
            .username("admin.edutiv@gmail.com")
            .password("admin123")
            .build()));
        
        ResponseEntity<Object> responseEntity = userService.getAllUser();
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        List<User> user = (List<User>) Objects.requireNonNull(response).getData();
        
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1, user.size());
    }

    @Test
    void getUserDetail_Success_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
            .id(1L)
            .firstname("admin")
            .lastname("edutiv")
            .username("admin.edutiv@gmail.com")
            .password("admin123")
            .build()));

        ResponseEntity<Object> responseEntity = userService.getUserDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        User user = (User) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, user.getId());
        assertEquals("admin", user.getFirstname());
        assertEquals("edutiv", user.getLastname());
        assertEquals("admin.edutiv@gmail.com", user.getUsername());
        assertEquals("admin123", user.getPassword());
    }

    @Test
    void getUserDetail_NotFound_Test() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userService.getUserDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void testGetUserDetailEmail_Success_Test() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(User.builder()
            .id(1L)
            .firstname("admin")
            .lastname("edutiv")
            .username("admin.edutiv@gmail.com")
            .password("admin123")
            .build()));

        ResponseEntity<Object> responseEntity = userService.getUserDetail("admin.edutiv@gmail.com");
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        User user = (User) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, user.getId());
        assertEquals("admin", user.getFirstname());
        assertEquals("edutiv", user.getLastname());
        assertEquals("admin.edutiv@gmail.com", user.getUsername());
        assertEquals("admin123", user.getPassword());
    }

    @Test
    void testGetUserDetailEmail_NotFound_Test() {
        when(userRepository.findByUsername("admin.edutiv@gmail.com")).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userService.getUserDetail("admin.edutiv@gmail.com");
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateUserbyUser_Success_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        User user = User.builder()
            .id(1L)
            .firstname("admin")
            .lastname("edutiv")
            .username("admin.edutiv@gmail.com")
            .password("admin123")
            .category(Category.builder().id(1L).build())
            .build();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        ResponseEntity<Object> responseEntity = userService.updateUserbyUser(UserDto.builder()
            .firstname("admin")
            .lastname("edutiv")
            .email("admin.edutiv@gmail.com")
            .password("admin123")
            .specializationId(1L)
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        User data = (User) Objects.requireNonNull(response).getData();

        assertEquals(1L, user.getId());
        assertEquals("admin", user.getFirstname());
        assertEquals("edutiv", user.getLastname());
        assertEquals("admin.edutiv@gmail.com", user.getUsername());
        assertEquals("admin123", user.getPassword());
    }

    @Test
    void updateUserbyUser_Exception_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        User user = User.builder()
            .id(1L)
            .firstname("admin")
            .lastname("edutiv")
            .username("admin.edutiv@gmail.com")
            .password("admin123")
            .category(Category.builder().id(1L).build())
            .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = userService.updateUserbyUser(UserDto.builder()
            .firstname("admin")
            .lastname("edutiv")
            .email("admin.edutiv@gmail.com")
            .password("admin123")
            .specializationId(1L)
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateUserbyUser_NotFound_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userService.updateUserbyUser(UserDto.builder()
            .firstname("admin")
            .lastname("edutiv")
            .email("admin.edutiv@gmail.com")
            .password("admin123")
            .specializationId(1L)
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateUserbyUser_CategoryNotFound_Test() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(User.builder()
            .id(1L)
            .firstname("admin")
            .lastname("edutiv")
            .username("admin.edutiv@gmail.com")
            .password("admin123")
            .build()));
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userService.updateUserbyUser(UserDto.builder()
            .firstname("admin")
            .lastname("edutiv")
            .email("admin.edutiv@gmail.com")
            .password("admin123")
            .specializationId(1L)
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateUserbyAdmin_Success_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        User user = User.builder()
            .id(1L)
            .firstname("admin")
            .lastname("edutiv")
            .username("admin.edutiv@gmail.com")
            .password("admin123")
            .category(Category.builder().id(1L).build())
            .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        ResponseEntity<Object> responseEntity = userService.updateUserbyAdmin(1L, UserDto.builder()
            .firstname("admin")
            .lastname("edutiv")
            .email("admin.edutiv@gmail.com")
            .password("admin123")
            .specializationId(1L)
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        User data = (User) Objects.requireNonNull(response).getData();

        assertEquals(1L, data.getId());
        assertEquals("admin", data.getFirstname());
        assertEquals("edutiv", data.getLastname());
        assertEquals("admin.edutiv@gmail.com", data.getUsername());
        assertEquals("admin123", data.getPassword());
        assertEquals(1L, data.getCategory().getId());
    }

    @Test
    void updateUserbyAdmin_Exception_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        User user = User.builder()
            .id(1L)
            .firstname("admin")
            .lastname("edutiv")
            .username("admin.edutiv@gmail.com")
            .password("admin123")
            .category(Category.builder().id(1L).build())
            .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = userService.updateUserbyAdmin(1L, UserDto.builder()
            .firstname("admin")
            .lastname("edutiv")
            .email("admin.edutiv@gmail.com")
            .password("admin123")
            .specializationId(1L)
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateUserbyAdmin_NotFound_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userService.updateUserbyAdmin(1L, UserDto.builder()
            .firstname("admin")
            .lastname("edutiv")
            .email("admin.edutiv@gmail.com")
            .password("admin123")
            .specializationId(1L)
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateUserbyAdmin_CategoryNotFound_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
            .id(1L)
            .firstname("admin")
            .lastname("edutiv")
            .username("admin.edutiv@gmail.com")
            .password("admin123")
            .build()));
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userService.updateUserbyAdmin(1L, UserDto.builder()
            .firstname("admin")
            .lastname("edutiv")
            .email("admin.edutiv@gmail.com")
            .password("admin123")
            .specializationId(1L)
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteUser_Success_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
            .id(1L)
            .lastname("edutiv")
            .username("admin.edutiv@gmail.com")
            .password("admin123")
            .build()));
        doNothing().when(userRepository).deleteById(anyLong());

        ApiResponse response = (ApiResponse) userService.deleteUser(1L).getBody();
        assertEquals("SUCCESS", response.getMessage());
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteUser_Exception_Test() {
        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        doNothing().when(userRepository).deleteById(anyLong()); 
        
        ResponseEntity<Object> responseEntity = userService.deleteUser(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteUser_NotFound_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(userRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = userService.deleteUser(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void ChangePassword_Success_Test() {
        when(userRepository.findByUsername(userDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userDto.getCurrentPassword(), user.getPassword())).thenReturn(true);
        
        var result = userService.changePassword(userDto);
        
        assertEquals(user, result);
    }
    
    @Test
    void changePassword_Exception_Test() {
        assertThrows(RuntimeException.class, () -> {
            userService.changePassword(userDto);
        });
    }

    @Test
    void changePasswordException2Test() {
        when(userRepository.findByUsername(userDto.getEmail()))
            .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> {
            userService.changePassword(userDto);
        });
    }

}
