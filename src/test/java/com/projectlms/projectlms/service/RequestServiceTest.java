package com.projectlms.projectlms.service;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projectlms.projectlms.domain.common.ApiResponse;
import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dao.RequestForm;
import com.projectlms.projectlms.domain.dao.Tool;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.domain.dto.RequestFormDto;
import com.projectlms.projectlms.domain.dto.ToolDto;
import com.projectlms.projectlms.repository.CategoryRepository;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.RequestRepository;
import com.projectlms.projectlms.repository.ToolRepository;
import com.projectlms.projectlms.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RequestService.class)
public class RequestServiceTest {

    @MockBean
    private RequestRepository requestRepository;

    @MockBean 
    private UserRepository userRepository;

    @MockBean 
    private CategoryRepository categoryRepository;

    @Autowired
    private RequestService requestService;

    @Test
    void addRequest_Success_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));

        when(requestRepository.save(any())).thenReturn(RequestForm.builder()
            .id(1L)
            .user(User.builder().id(1L).build())
            .title("Mastering UIUX Adobe XD")
            .category(Category.builder().id(1L).build())
            .requestType("Course")
            .build()
        );

        ResponseEntity<Object> responseEntity = requestService.addRequest(RequestFormDto.builder()
            .id(1L)
            .userId(1L)
            .title("Mastering UIUX Adobe XD")
            .categoryId(1L)
            .requestType("Course")
            .build()
        );
        
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        RequestForm requestForm = (RequestForm) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, requestForm.getId());
        assertEquals("Mastering UIUX Adobe XD", requestForm.getTitle());
        assertEquals("Course", requestForm.getRequestType());
    }

    @Test
    void addRequest_Exception_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(requestRepository.save(any())).thenThrow(NullPointerException.class);

        ResponseEntity<Object> responseEntity = requestService.addRequest(RequestFormDto.builder()
            .id(1L)
            .userId(1L)
            .title("Mastering UIUX Adobe XD")
            .categoryId(1L)
            .requestType("Course")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void addRequest_CategoryNotFound_Test() {
        when(categoryRepository.searchCategoryById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = requestService.addRequest(RequestFormDto.builder()
            .id(1L)
            .userId(1L)
            .title("Mastering UIUX Adobe XD")
            .categoryId(1L)
            .requestType("Course")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void addRequest_UserNotFound_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = requestService.addRequest(RequestFormDto.builder()
            .id(1L)
            .userId(1L)
            .title("Mastering UIUX Adobe XD")
            .categoryId(1L)
            .requestType("Course")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllRequest_Success_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));

        when(requestRepository.findAll()).thenReturn(List.of(RequestForm.builder()
            .id(1L)
            .user(User.builder().id(1L).build())
            .title("Mastering UIUX Adobe XD")
            .category(Category.builder().id(1L).build())
            .requestType("Course")
            .build()
        ));

        ResponseEntity<Object> responseEntity = requestService.getAllRequest();
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        List<RequestForm> requestForms = (List<RequestForm>) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(1,requestForms.size());
    }

    @Test
    void getAllRequest_Exception_Test() {
        when(requestRepository.findAll()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = requestService.getAllRequest();
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getRequestDetail_Success_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));

        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(RequestForm.builder()
            .id(1L)
            .user(User.builder().id(1L).build())
            .title("Mastering UIUX Adobe XD")
            .category(Category.builder().id(1L).build())
            .requestType("Course")
            .build()
        ));

        ResponseEntity<Object> responseEntity = requestService.getRequestDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        RequestForm requestForm = (RequestForm) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, requestForm.getId());
        assertEquals("Mastering UIUX Adobe XD", requestForm.getTitle());
        assertEquals("Course", requestForm.getRequestType());
    }

    @Test
    void getRequestDetail_Exception_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));

        when(requestRepository.findById(1L)).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = requestService.getRequestDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getRequestDetail_NotFound_Test() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = requestService.getRequestDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteRequest_Success_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));

        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(RequestForm.builder()
            .id(1L)
            .user(User.builder().id(1L).build())
            .title("Mastering UIUX Adobe XD")
            .category(Category.builder().id(1L).build())
            .requestType("Course")
            .build()
        ));
        doNothing().when(requestRepository).deleteById(anyLong());

        ApiResponse response = (ApiResponse) requestService.deleteRequest(1L).getBody();
        assertEquals("SUCCESS", response.getMessage());
        verify(requestRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteRequest_Exception_Test() {
        when(requestRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        doNothing().when(requestRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = requestService.deleteRequest(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteRequest_NotFound_Test() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(requestRepository).deleteById(anyLong()); 
        
        ResponseEntity<Object> responseEntity = requestService.deleteRequest(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }
}
