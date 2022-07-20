package com.projectlms.projectlms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projectlms.projectlms.domain.common.ApiResponse;
import com.projectlms.projectlms.domain.dao.Category;
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dto.CourseDto;
import com.projectlms.projectlms.repository.CategoryRepository;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.SectionRepository;
import com.projectlms.projectlms.repository.ToolRepository;
import com.projectlms.projectlms.repository.UserRepository;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseService.class)
public class CourseServiceTest {

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private SectionRepository sectionRepository;

    @MockBean
    private ToolRepository toolRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;
    
    @Test
    void addCourse_Success_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(courseRepository.save(any())).thenReturn(Course.builder()
            .id(1L)
            .courseName("courseName")
            .courseImage("courseImage")
            .category(Category.builder().id(1L).build())
            .description("description")
            .totalVideo(10)
            .totalTimes("totalTimes")
            .build()
        );
        
        ResponseEntity<Object> responseEntity = courseService.addCourse(CourseDto.builder()
            .courseName("courseName")
            .categoryId(1L)
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Course course = (Course) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, course.getId());
        assertEquals("courseName", course.getCourseName());
        assertEquals("courseImage", course.getCourseImage());
        assertEquals(1L, course.getCategory().getId());
        assertEquals("description", course.getDescription());
        assertEquals(10, course.getTotalVideo());
        assertEquals("totalTimes", course.getTotalTimes());
    }

    @Test
    void addCourse_Exception_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(courseRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.addCourse(CourseDto.builder()
            .id(1L)
            .courseName("courseName")
            .categoryId(1L)
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void addCourse_NotFound_Test() {
        when(categoryRepository.searchCategoryById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = courseService.addCourse(CourseDto.builder()
            .id(1L)
            .courseName("courseName")
            .courseImage("courseImage")
            .categoryId(1L)
            .description("description")
            .totalVideo(10)
            .totalTimes("totalTimes")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllCourse_Success_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));

        when(courseRepository.findAll()).thenReturn(List.of(Course.builder()
                .id(1L)
                .courseName("Introduction to Backend Enginner with Java Spring Boot")
                .courseImage("https://i.pinimg.com/564x/b9/b7/0a/b9b70ae0c0b202aa4994d11be7dd57f4.jpg")
                .category(Category.builder().id(1L).build())
                .description("Java Spring Boot is a multifunctional pogramming language that is widely used by large Indonesian companies such as Toopedia, Gojek and many more.")
                .totalVideo(12)
                .totalTimes("1h 35m")
                .build()
        ));
        ResponseEntity<Object> responseEntity = courseService.getAllCourse();
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        List<Course> courses = (List<Course>) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(1,courses.size());
    }

    @Test
    void getAllCourse_Exception_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(courseRepository.findAll()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.getAllCourse();
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllCourse_NotFound_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.empty());
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = courseService.getAllCourse();
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getCourseDetail_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder()
                .id(1L)
                .courseName("Introduction to Backend Enginner with Java Spring Boot")
                .courseImage("https://i.pinimg.com/564x/b9/b7/0a/b9b70ae0c0b202aa4994d11be7dd57f4.jpg")
                .category(Category.builder().id(1L).build())
                .description("Java Spring Boot is a multifunctional pogramming language that is widely used by large Indonesian companies such as Toopedia, Gojek and many more.")
                .totalVideo(12)
                .totalTimes("1h 35m")
                .build()
        ));

        ResponseEntity<Object> responseEntity = courseService.getCourseDetail( 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Course course = (Course) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, course.getId());
        assertEquals("Introduction to Backend Enginner with Java Spring Boot", course.getCourseName());
        assertEquals("https://i.pinimg.com/564x/b9/b7/0a/b9b70ae0c0b202aa4994d11be7dd57f4.jpg", course.getCourseImage());
        assertEquals("Java Spring Boot is a multifunctional pogramming language that is widely used by large Indonesian companies such as Toopedia, Gojek and many more.", course.getDescription());
        assertEquals(12, course.getTotalVideo());
        assertEquals("1h 35m", course.getTotalTimes());
    }

    @Test
    void getCourseDetail_Exception_Test() {
            when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
            when(courseRepository.searchCourseById(1L)).thenThrow(NullPointerException.class);
            ResponseEntity<Object> responseEntity = courseService.getCourseDetail(1L);
            ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getCourseDetail_NotFound_Test() {
            when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
            ResponseEntity<Object> responseEntity = courseService.getCourseDetail(1L);
            ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateCourse_Success_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        Course course = Course.builder()
            .id(1L)
            .courseName("courseName")
            .courseImage("courseImage")
            .category(Category.builder().id(1L).build())
            .description("description")
            .totalVideo(10)
            .totalTimes("totalTimes")
            .build();
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(course));
        when(courseRepository.save(any())).thenReturn(course);
        ResponseEntity<Object> responseEntity = courseService.updateCourse(1L, CourseDto.builder()
            .courseName("courseName")
            .courseImage("courseImage")
            .categoryId(1L)
            .description("description")
            .totalVideo(10)
            .totalTimes("totalTimes")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Course data = (Course) Objects.requireNonNull(response).getData();

        assertEquals(1L, data.getId());
        assertEquals("courseName", data.getCourseName());
        assertEquals("courseImage", data.getCourseImage());
        assertEquals(1L, data.getCategory().getId());
        assertEquals("description", data.getDescription());
        assertEquals(10, data.getTotalVideo());
        assertEquals("totalTimes", data.getTotalTimes());
    }

    @Test
    void updateCourse_Exception_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        Course course = Course.builder()
            .id(1L)
            .courseName("courseName")
            .courseImage("courseImage")
            .category(Category.builder().id(1L).build())
            .description("description")
            .totalVideo(10)
            .totalTimes("totalTimes")
            .build();
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(course));
        when(courseRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.updateCourse(1L, CourseDto.builder()
            .courseName("courseName")
            .courseImage("courseImage")
            .categoryId(1L)
            .description("description")
            .totalVideo(10)
            .totalTimes("totalTimes")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateCourse_CategoryNotFound_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = courseService.updateCourse(1L, CourseDto.builder()
            .courseName("courseName")
            .courseImage("courseImage")
            .categoryId(1L)
            .description("description")
            .totalVideo(10)
            .totalTimes("totalTimes")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateCourse_CourseNotFound_Test() {
        when(categoryRepository.searchCategoryById(anyLong())).thenReturn(Optional.of(Category.builder().id(1L).build()));
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = courseService.updateCourse(1L, CourseDto.builder()
            .courseName("courseName")
            .courseImage("courseImage")
            .categoryId(1L)
            .description("description")
            .totalVideo(10)
            .totalTimes("totalTimes")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteCourse_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder()
            .id(1L)
            .courseName("courseName")
            .courseImage("courseImage")
            .category(Category.builder().id(1L).build())
            .description("description")
            .totalVideo(10)
            .totalTimes("totalTimes")
            .build()
        ));
        doNothing().when(courseRepository).deleteById(anyLong());

        ApiResponse response = (ApiResponse) courseService.deleteCourse(1L).getBody();
        assertEquals("SUCCESS", response.getMessage());
        verify(courseRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteCourse_NotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(courseRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = courseService.deleteCourse(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteCourse_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenThrow(NullPointerException.class);
        doNothing().when(courseRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = courseService.deleteCourse(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    // @Test
    // void getCourseByUserSpec_Success_Test() {

    // }

    // @Test
    // void getCourseByUserSpec_Exception_Test() {

    // }

    // @Test
    // void getCourseByUserSpec_Exception2_Test() {

    // }

    // @Test
    // void getCourseByUserSpec_NotFound_Test() {

    // }

    // @Test
    // void searchByCourseName_Success_Test() {

    // }

    // @Test
    // void searchByCourseName_NotFound_Test() {

    // }

    // @Test
    // void searchByCourseName_Exception_Test() {

    // }
}
