package com.projectlms.projectlms.service;

import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.projectlms.projectlms.domain.dao.Material;
import com.projectlms.projectlms.domain.dao.Report;
import com.projectlms.projectlms.domain.dao.RoleEnum;
import com.projectlms.projectlms.domain.dao.Section;
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
    private List<EnrolledCourse> enrolledCourses;
    private EnrolledCourse enrolledCourse;
    private EnrolledCourseDto enrolledCourseDto;
    private User user;
    private Course course;
    private Material material;
    private Long id;
    private Integer totalMaterials;
    private Boolean check;

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

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
        enrolledCourses= EASY_RANDOM.objects(EnrolledCourse.class, 2).collect(Collectors.toList());
        enrolledCourse = EASY_RANDOM.nextObject(EnrolledCourse.class);
        enrolledCourseDto = EASY_RANDOM.nextObject(EnrolledCourseDto.class);
        user = EASY_RANDOM.nextObject(User.class);
        course = EASY_RANDOM.nextObject(Course.class);

        enrolledCourseDto.setCourseId(course.getId());
        enrolledCourseDto.setEmail(user.getUsername());
        enrolledCourseDto.setId(enrolledCourse.getId());

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
        when(userRepository.findUsername(user.getUsername())).thenReturn(user);
        when(enrolledCourseRepository.getEnrolledCourseByUser(user.getId())).thenReturn(enrolledCourses);
        var result = enrolledCourseService.getEnrolledCourseByUser(user.getUsername());
        assertEquals(enrolledCourses, result);
    }

    @Test
    void getEnrolledCourseByUser_Exception_Test() {
        assertThrows(RuntimeException.class, () -> {
            enrolledCourseService.getEnrolledCourseByUser(user.getUsername());
        });
    }

    @Test
    void getEnrolledCourseByUser_Exception2_Test() {
        when(userRepository.findUsername(user.getUsername())).thenReturn(user);
        assertThrows(RuntimeException.class, () -> {
            enrolledCourseService.getEnrolledCourseByUser(user.getUsername());
        });
    }

    @Test
    void updateRatingReview_Success_Test() {
        when(enrolledCourseRepository.findById(enrolledCourse.getId()))
            .thenReturn(Optional.of(enrolledCourse));
        enrolledCourse.setUser(user);
        when(courseRepository.findById(enrolledCourse.getCourse().getId()))
            .thenReturn(Optional.of(course));
        
        Integer ratingEnrolledCourse = 4;
        when(enrolledCourseRepository.countRatingByCourse(course.getId()))
            .thenReturn(ratingEnrolledCourse);
        var result = enrolledCourseService.updateRatingReview(user.getUsername(), enrolledCourseDto);
        assertEquals(enrolledCourse, result);
    }

    @Test
    void updateRatingReview_Success2_Test() {
        when(enrolledCourseRepository.findById(enrolledCourse.getId()))
            .thenReturn(Optional.of(enrolledCourse));
        enrolledCourse.setUser(user);
        when(courseRepository.findById(enrolledCourse.getCourse().getId()))
            .thenReturn(Optional.of(course));
        var result = enrolledCourseService.updateRatingReview(user.getUsername(), enrolledCourseDto);
        assertEquals(enrolledCourse, result);
    }

    @Test
    void updateRatingReview_Exception_Test() {
        assertThrows(RuntimeException.class, () -> {
            enrolledCourseService.updateRatingReview(user.getUsername(), enrolledCourseDto);
        });
    }

    @Test
    void updateRatingReview_Exception2_Test() {
        when(enrolledCourseRepository.findById(enrolledCourse.getId()))
            .thenReturn(Optional.of(enrolledCourse));
        User random = EASY_RANDOM.nextObject(User.class);
        enrolledCourse.setUser(random);

        assertThrows(RuntimeException.class, () -> {
            enrolledCourseService.updateRatingReview(user.getUsername(), enrolledCourseDto);
        });
    }

    @Test
    void updateRatingReview_Exception3_Test() {
        when(enrolledCourseRepository.findById(enrolledCourse.getId()))
            .thenReturn(Optional.of(enrolledCourse));
        enrolledCourse.setUser(user);

        assertThrows(RuntimeException.class, () -> {
            enrolledCourseService.updateRatingReview(user.getUsername(), enrolledCourseDto);
        });
    }

    @Test
    void updateProgress_Exception_Test() {
        assertThrows(RuntimeException.class, () -> {
            enrolledCourseService.updateProgress(enrolledCourse.getId(), enrolledCourseDto);
        });
    }

    @Test
    void updateProgress_Exception2_Test() {
        when(enrolledCourseRepository.findById(enrolledCourse.getId()))
            .thenReturn(Optional.of(enrolledCourse));
        when(userRepository.findUsername(enrolledCourseDto.getEmail()))
            .thenReturn(user);
        user.getRoles().forEach(role -> {
            role.setName(RoleEnum.ROLE_USER);
        });
        assertThrows(RuntimeException.class, () -> {
            enrolledCourseService.updateProgress(enrolledCourse.getId(), enrolledCourseDto);
        });
    }

    @Test
    void updateProgress_Exception3_Test() {
        when(enrolledCourseRepository.findById(enrolledCourse.getId()))
            .thenReturn(Optional.of(enrolledCourse));
        when(userRepository.findUsername(enrolledCourseDto.getEmail()))
            .thenReturn(user);
        user.getRoles().forEach(role -> {
            role.setName(RoleEnum.ROLE_ADMIN);
        });
        List<Section> sections = sectionRepository.searchAllSections(enrolledCourse.getCourse().getId());

        sections.forEach(section -> {
            List<Material> materials = section.getMaterials();
            materials.forEach(checkMaterial -> {
                checkMaterial.setId(enrolledCourseDto.getMaterialId());
                if(checkMaterial.getId().equals(enrolledCourseDto.getMaterialId())) {
                    material = checkMaterial;
                    check = true;
                }
                checkMaterial.setIsDeleted(true);
            });
        });
        assertThrows(RuntimeException.class, () -> {
            enrolledCourseService.updateProgress(enrolledCourse.getId(), enrolledCourseDto);
        });
    }

    @Test
    void updateProgress_Exception4_Test() {
        when(enrolledCourseRepository.findById(enrolledCourse.getId()))
            .thenReturn(Optional.of(enrolledCourse));
        when(userRepository.findUsername(enrolledCourseDto.getEmail()))
            .thenReturn(user);
        user.getRoles().forEach(role -> {
            role.setName(RoleEnum.ROLE_ADMIN);
        });
        check = false;
        assertThrows(RuntimeException.class, () -> {
            enrolledCourseService.updateProgress(enrolledCourse.getId(), enrolledCourseDto);
        });
    }
}
