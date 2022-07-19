package com.projectlms.projectlms.service;

import static org.mockito.Mockito.when;

import java.util.Collections;
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
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dao.Section;
import com.projectlms.projectlms.domain.dto.SectionDto;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.EnrolledCourseRepository;
import com.projectlms.projectlms.repository.MaterialRepository;
import com.projectlms.projectlms.repository.ReportRepository;
import com.projectlms.projectlms.repository.SectionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SectionService.class)
public class SectionServiceTest {
    @MockBean
    private SectionRepository sectionRepository;

    @MockBean 
    private CourseRepository courseRepository;

    @MockBean
    private MaterialRepository materialRepository;

    @MockBean 
    private ReportRepository reportRepository;

    @MockBean 
    private EnrolledCourseRepository enrolledCourseRepository;

    @Autowired
    private SectionService sectionService;
    
    @Test
    void addSection_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        
        when(sectionRepository.save(any())).thenReturn(Section.builder()
            .id(1L)
            .course(Course.builder().id(1L).build())
            .sectionName("Basic Programming")
            .build());

        ResponseEntity<Object> responseEntity = sectionService.addSection(SectionDto.builder()
            .courseId(1L)
            .sectionName("Basic Programming")
            .build()
        );

        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Section section = (Section) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(1L,section.getId());
        assertEquals(1L,section.getCourse().getId());
        assertEquals("Basic Programming",section.getSectionName());
    }

    @Test
    void addSection_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = sectionService.addSection(SectionDto.builder()
            .id(1L)
            .courseId(1L)
            .sectionName("Basic Programming")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void addSection_NotFound_Test() {
        when(courseRepository.searchCourseById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = sectionService.addSection(SectionDto.builder()
            .id(1L)
            .courseId(1L)
            .sectionName("Basic Programming")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllSection_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchAllSections(anyLong())).thenReturn(List.of(Section.builder()
                .id(1L)
                .course(Course.builder().id(1L).build())
                .sectionName("Basic Programming")
                .build()
        ));
        ResponseEntity<Object> responseEntity = sectionService.getAllSection(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        List<Section> sections = (List<Section>) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(1,sections.size());
    }

    @Test
    void getAllSection_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchAllSections(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = sectionService.getAllSection(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllSection_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = sectionService.getAllSection(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllSection_SectionNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchAllSections(anyLong())).thenReturn(Collections.emptyList());
        ResponseEntity<Object> responseEntity = sectionService.getAllSection(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getSectionDetail_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder()
            .id(1L)
            .course(Course.builder().id(1L).build())
            .sectionName("Basic Programming")
            .build()
        ));
        ResponseEntity<Object> responseEntity = sectionService.getSectionDetail(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Section section = (Section) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, section.getId());
        assertEquals(1L,section.getCourse().getId());
        assertEquals("Basic Programming",section.getSectionName());
    }

    @Test
    void getSectionDetail_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(1L, 1L)).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = sectionService.getSectionDetail(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getSectionDetail_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = sectionService.getSectionDetail(1L,1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getSectionDetail_SectionNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = sectionService.getSectionDetail(1L,1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateSection_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        Section section = Section.builder()
            .id(1L)
            .course(Course.builder().id(1L).build())
            .sectionName("Basic Programming")
            .build();
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(section));
        when(sectionRepository.save(any())).thenReturn(section);
        ResponseEntity<Object> responseEntity = sectionService.updateSection(1L, SectionDto.builder()
            .courseId(1L)
            .sectionName("Basic Programming")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Section data = (Section) Objects.requireNonNull(response).getData();

        assertEquals(1L, data.getId());
        assertEquals(1L, data.getCourse().getId());
        assertEquals("Basic Programming", data.getSectionName());
    }

    @Test
    void updateSection_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        Section section = Section.builder()
            .id(1L)
            .course(Course.builder().id(1L).build())
            .sectionName("Basic Programming")
            .build();
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(section));
        when(sectionRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = sectionService.updateSection(1L, SectionDto.builder()
            .courseId(1L)
            .sectionName("Basic Programming")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateSection_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = sectionService.updateSection(1L, SectionDto.builder()
            .courseId(1L)
            .sectionName("Basic Programming")
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateSection_SectionNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = sectionService.updateSection(1L, SectionDto.builder()
            .courseId(1L)
            .sectionName("Basic Programming")
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteSection_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong()))
            .thenReturn(Optional.of(Section.builder()
                .id(1L)
                .course(Course.builder().id(1L).build())
                .sectionName("Basic Programming")
                .build()));
        doNothing().when(sectionRepository).deleteById(anyLong());

        ApiResponse response = (ApiResponse) sectionService.deleteSection(1L, 1L).getBody();
        assertEquals("SUCCESS", response.getMessage());
        verify(sectionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteSection_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenThrow(NullPointerException.class);
        doNothing().when(sectionRepository).deleteById(anyLong()); 
        
        ResponseEntity<Object> responseEntity = sectionService.deleteSection(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteSection_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(sectionRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = sectionService.deleteSection(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteSection_SectionNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.empty());
        doNothing().when(sectionRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = sectionService.deleteSection(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }
}
