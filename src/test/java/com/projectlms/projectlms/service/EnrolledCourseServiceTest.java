package com.projectlms.projectlms.service;


import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projectlms.projectlms.domain.common.ApiResponse;
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dao.EnrolledCourse;
import com.projectlms.projectlms.domain.dao.User;
import com.projectlms.projectlms.domain.dto.EnrolledCourseDto;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.EnrolledCourseRepository;
import com.projectlms.projectlms.repository.ReportRepository;
import com.projectlms.projectlms.repository.SectionRepository;
import com.projectlms.projectlms.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EnrolledCourseService.class)
public class EnrolledCourseServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private Long id;

    @MockBean
    private EnrolledCourseRepository enrolledCourseRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private SectionRepository sectionRepository;

    @MockBean
    private ReportService reportService;

    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = EASY_RANDOM.nextObject(Long.class);
    }

    @Test
    void addEnrolledCourse_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));

        when(enrolledCourseRepository.save(any())).thenReturn(EnrolledCourse.builder()
            .id(1L)
            .user(User.builder().id(1L).build())
            .course(Course.builder().id(1L).build())
            .build()  
        );

        ResponseEntity<Object> responseEntity = enrolledCourseService.addEnrolledCourse(EnrolledCourseDto.builder()
            .id(1L)
            .userId(1L)
            .courseId(1L)
            .build()
        );

        ApiResponse response = (ApiResponse) responseEntity.getBody();
        EnrolledCourse enrolledCourse = (EnrolledCourse) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, enrolledCourse.getId());
        assertEquals(1L, enrolledCourse.getUser().getId());
        assertEquals(1L, enrolledCourse.getCourse().getId());
    }

    @Test
    void addEnrolledCourse_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(enrolledCourseRepository.save(any())).thenThrow(NullPointerException.class);

        ResponseEntity<Object> responseEntity = enrolledCourseService.addEnrolledCourse(EnrolledCourseDto.builder()
            .id(1L)
            .userId(1L)
            .courseId(1L)
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void addEnrolledCourse_CourseNotFound_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(courseRepository.searchCourseById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = enrolledCourseService.addEnrolledCourse(EnrolledCourseDto.builder()
            .id(1L)
            .userId(1L)
            .courseId(1L)
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void addEnrolledCourse_UserNotFound_Test() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = enrolledCourseService.addEnrolledCourse(EnrolledCourseDto.builder()
            .id(1L)
            .userId(1L)
            .courseId(1L)
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllEnrolledCourse_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));

        when(enrolledCourseRepository.findAll()).thenReturn(List.of(EnrolledCourse.builder()
            .id(1L)
            .user(User.builder().id(1L).build())
            .course(Course.builder().id(1L).build())
            .build()  
        ));

        ResponseEntity<Object> responseEntity = enrolledCourseService.getAllEnrolledCourse();
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        List<EnrolledCourse> enrolledCourses = (List<EnrolledCourse>) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(1,enrolledCourses.size());
    }

    @Test
    void getAllEnrolledCourse_Exception_Test() {
        when(enrolledCourseRepository.findAll()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = enrolledCourseService.getAllEnrolledCourse();
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getEnrolledCourseDetail_Success_Test() {
        EnrolledCourse enrolledCourse = EASY_RANDOM.nextObject(EnrolledCourse.class);

        when(enrolledCourseRepository.findById(enrolledCourse.getId()))
            .thenReturn(Optional.of(enrolledCourse));
        var result = enrolledCourseService.getEnrolledCourseDetail(enrolledCourse.getId());
        assertEquals(enrolledCourse, result);
    }

    @Test
    void getEnrolledCourseDetail_Exception_Test() {
        assertThrows(RuntimeException.class, () -> {
            enrolledCourseService.getEnrolledCourseDetail(id);
        });
    }

    @Test
    void deleteEnrolledCourse_Success_Test() {
        when(enrolledCourseRepository.findById(anyLong())).thenReturn(Optional.of(EnrolledCourse.builder()
            .id(1L)
            .user(User.builder().id(1L).build())
            .course(Course.builder().id(1L).build())
            .build()
        ));
        doNothing().when(enrolledCourseRepository).deleteById(anyLong());

        ApiResponse response = (ApiResponse) enrolledCourseService.deleteEnrolledCourse(1L).getBody();
        assertEquals("SUCCESS", response.getMessage());
        verify(enrolledCourseRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteEnrolledCourse_Exception_Test() {
        when(enrolledCourseRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        doNothing().when(enrolledCourseRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = enrolledCourseService.deleteEnrolledCourse(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteEnrolledCourse_NotFound_Test() {
        when(enrolledCourseRepository.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(enrolledCourseRepository).deleteById(anyLong()); 
        
        ResponseEntity<Object> responseEntity = enrolledCourseService.deleteEnrolledCourse(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getEnrolledCourseByCourse_Success_Test() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(enrolledCourseRepository.getEnrolledCourseByCourse(anyLong())).thenReturn(List.of(EnrolledCourse.builder()
            .id(1L)
            .user(User.builder().id(1L).build())
            .course(Course.builder().id(1L).build())
            .build()));
        ResponseEntity<Object> responseEntity = enrolledCourseService.getEnrolledCourseByCourse(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        List<EnrolledCourse> enrolledCourses = (List<EnrolledCourse>) Objects.requireNonNull(response).getData();
            
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1, enrolledCourses.size());
    }

    @Test
    void getEnrolledCourseByCourse_CourseNotFound_Test() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = enrolledCourseService.getEnrolledCourseByCourse(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getEnrolledCourseByCourse_EnrolledCourseNotFound_Test() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(enrolledCourseRepository.getEnrolledCourseByCourse(anyLong())).thenReturn(Collections.emptyList());
        ResponseEntity<Object> responseEntity = enrolledCourseService.getEnrolledCourseByCourse(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getEnrolledCourseByCourse_Exception_Test() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(enrolledCourseRepository.getEnrolledCourseByCourse(1L)).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = enrolledCourseService.getEnrolledCourseByCourse(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getEnrolledCourseByUser_Success_Test() {

    }

    @Test
    void getEnrolledCourseByUser_NotFound_Test() {

    }

    @Test
    void getEnrolledCourseByUser_Exception_Test() {

    }

    @Test
    void updateRatingReview_Success_Test() {

    }

    @Test
    void updateRatingReview_Exception_Test() {

    }

    @Test
    void updateRatingReview_NotFound_Test() {

    }

    @Test
    void updateProgress_Success_Test() {

    }

    @Test
    void updateProgress_Exception_Test() {

    }

    @Test
    void updateProgress_NotFound_Test() {

    }
}
