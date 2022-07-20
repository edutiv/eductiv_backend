package com.projectlms.projectlms.service;

import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// import org.jeasy.random.EasyRandom;
// import org.junit.jupiter.api.BeforeEach;
// import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.projectlms.projectlms.domain.common.ApiResponse;
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dao.EnrolledCourse;
import com.projectlms.projectlms.domain.dao.Material;
import com.projectlms.projectlms.domain.dao.Section;
import com.projectlms.projectlms.domain.dto.MaterialDto;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.EnrolledCourseRepository;
import com.projectlms.projectlms.repository.MaterialRepository;
import com.projectlms.projectlms.repository.ReportRepository;
import com.projectlms.projectlms.repository.SectionRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MaterialService.class)
public class MaterialServiceTest {
    // private final EasyRandom EASY_RANDOM = new EasyRandom();
    // private List<Material> materials;
    // private MaterialDto materialDto;
    // private Material material;
    // private Section section;
    // private Course course;

    @MockBean
    private MaterialRepository materialRepository;

    @MockBean
    private SectionRepository sectionRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private EnrolledCourseRepository enrolledCourseRepository;

    @MockBean
    private ReportRepository reportRepository;

    @Autowired
    private MaterialService materialService;

    @Test
    void addMaterial_Success_Test() {
        // when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        // when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));

        // when(materialRepository.save(any())).thenReturn(Material.builder()
        //     .id(1L)
        //     .section(Section.builder().id(1L).build())
        //     .materialType("video")
        //     .materialName("Introduction Java Spring Boot")
        //     .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
        //     .build()
        // );

        // ResponseEntity<Object> responseEntity = materialService.addMaterial(MaterialDto.builder()
        //     .sectionId(1L)
        //     .materialType("video")
        //     .materialName("Introduction Java Spring Boot")
        //     .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
        //     .build()
        // );

        // ApiResponse response = (ApiResponse) responseEntity.getBody();
        // Material material = (Material) Objects.requireNonNull(response).getData();
        // assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
        // assertEquals("SUCCESS",response.getMessage());
        // assertEquals(1L, material.getId());
        // assertEquals(1L, material.getSection().getId());
        // assertEquals("video", material.getMaterialType());
        // assertEquals("Introduction Java Spring Boot", material.getMaterialName());
        // assertEquals("https://www.youtube.com/embed/bk94VQBFS_I", material.getMaterialUrl());
    }

    @Test
    void addMaterial_Exception_Test() {
        // when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        // when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        // //when(enrolledCourseRepository.getEnrolledCourseByCourse(anyLong())).thenReturn(List.of(EnrolledCourse.builder().id(1L).build()));
        
        // when(materialRepository.save(any())).thenThrow(NullPointerException.class);
        // ResponseEntity<Object> responseEntity = materialService.addMaterial(MaterialDto.builder()
        //     .id(1L)
        //     .sectionId(1L)
        //     .materialType("video")
        //     .materialName("Introduction Java Spring Boot")
        //     .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
        //     .build()
        // );
        // ApiResponse response = (ApiResponse) responseEntity.getBody();
        // assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),responseEntity.getStatusCodeValue());
        // assertEquals("HAPPENED_ERROR",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void addMaterial_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = materialService.addMaterial(MaterialDto.builder()
            .id(1L)
            .sectionId(1L)
            .materialType("video")
            .materialName("Introduction Java Spring Boot")
            .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void addMaterial_SectionNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(1L, 1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = materialService.addMaterial(MaterialDto.builder()
            .id(1L)
            .sectionId(1L)
            .materialType("video")
            .materialName("Introduction Java Spring Boot")
            .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllMaterial_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        
        when(materialRepository.searchAllMaterials(anyLong())).thenReturn(List.of(Material.builder()
            .id(1L)
            .section(Section.builder().id(1L).build())
            .materialType("video")
            .materialName("Introduction Java Spring Boot")
            .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
            .build()
        ));

        ResponseEntity<Object> responseEntity = materialService.getAllMaterial(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        List<Material> materials = (List<Material>) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(1,materials.size());
    }

    @Test
    void getAllMaterial_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        when(materialRepository.searchAllMaterials(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = materialService.getAllMaterial(1L, 1l);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllMaterial_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = materialService.getAllMaterial(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllMaterial_SectionNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = materialService.getAllMaterial(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllMaterial_MaterialNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        when(materialRepository.searchAllMaterials(anyLong())).thenReturn(Collections.emptyList());
        ResponseEntity<Object> responseEntity = materialService.getAllMaterial(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getMaterialDetail_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));

        when(materialRepository.searchMaterialById(anyLong(), anyLong())).thenReturn(Optional.of(Material.builder()
            .id(1L)
            .section(Section.builder().id(1L).build())
            .materialType("video")
            .materialName("Introduction Java Spring Boot")
            .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
            .build()
        ));

        ResponseEntity<Object> responseEntity = materialService.getMaterialDetail(1L, 1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Material material = (Material) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, material.getId());
        assertEquals(1L, material.getSection().getId());
        assertEquals("video", material.getMaterialType());
        assertEquals("Introduction Java Spring Boot", material.getMaterialName());
        assertEquals("https://www.youtube.com/embed/bk94VQBFS_I", material.getMaterialUrl());
    }

    @Test
    void getMaterialDetail_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        when(materialRepository.searchMaterialById(anyLong(), anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = materialService.getMaterialDetail(1L, 1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getMaterialDetail_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = materialService.getMaterialDetail(1L,1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getMaterialDetail_SectionNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = materialService.getMaterialDetail(1L,1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getMaterialDetail_MaterialNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        when(materialRepository.searchMaterialById(anyLong(), anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = materialService.getMaterialDetail(1L,1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateMaterial_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        Material material = Material.builder()
            .id(1L)
            .section(Section.builder().id(1L).build())
            .materialType("video")
            .materialName("Introduction Java Spring Boot")
            .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
            .build();
        when(materialRepository.searchMaterialById(anyLong(), anyLong())).thenReturn(Optional.of(material));
        when(materialRepository.save(any())).thenReturn(material);
        ResponseEntity<Object> responseEntity = materialService.updateMaterial(1L, MaterialDto.builder()
            .sectionId(1L)
            .materialType("video")
            .materialName("Introduction Java Spring Boot")
            .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Material data = (Material) Objects.requireNonNull(response).getData();

        assertEquals(1L, material.getId());
        assertEquals(1L, material.getSection().getId());
        assertEquals("video", material.getMaterialType());
        assertEquals("Introduction Java Spring Boot", material.getMaterialName());
        assertEquals("https://www.youtube.com/embed/bk94VQBFS_I", material.getMaterialUrl());
    }

    @Test
    void updateMaterial_Exception_Test() {
        // when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        // when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        
        // Material material = Material.builder()
        //     .id(1L)
        //     .materialType("video")
        //     .materialName("Introduction Java Spring Boot")
        //     .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
        //     .section(Section.builder().id(1L).build())
        //     .build();
        // when(materialRepository.searchMaterialById(anyLong(), anyLong())).thenReturn(Optional.of(material));
        
        // when(materialRepository.save(any())).thenThrow(NullPointerException.class);
        // ResponseEntity<Object> responseEntity = materialService.updateMaterial(1L, MaterialDto.builder()
        //     .materialType("video")
        //     .materialName("Introduction Java Spring Boot")
        //     .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
        //     .sectionId(1L)
        //     .build()
        // );
        // ApiResponse response = (ApiResponse) responseEntity.getBody();
        // assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        // assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateMaterial_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = materialService.updateMaterial(1L, MaterialDto.builder()
                .materialType("video")
                .materialName("Introduction Java Spring Boot")
                .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
                .sectionId(1L)
                .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateMaterial_SectionNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(1L, 1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = materialService.updateMaterial(1L, MaterialDto.builder()
                .materialType("video")
                .materialName("Introduction Java Spring Boot")
                .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
                .sectionId(1L)
                .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());

    }

    @Test
    void updateMaterial_MaterialNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        when(materialRepository.searchMaterialById(1L, 1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = materialService.updateMaterial(1L, MaterialDto.builder()
                .materialType("video")
                .materialName("Introduction Java Spring Boot")
                .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
                .sectionId(1L)
                .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());

    }

    @Test
    void deleteMaterial_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        when(materialRepository.searchMaterialById(anyLong(), anyLong())).thenReturn(Optional.of(Material.builder()
            .id(1L)
            .section(Section.builder().id(1L).build())
            .materialType("video")
            .materialName("Introduction Java Spring Boot")
            .materialUrl("https://www.youtube.com/embed/bk94VQBFS_I")
            .build()
        ));
        doNothing().when(materialRepository).deleteById(anyLong());
        ApiResponse response = (ApiResponse) materialService.deleteMaterial(1L, 1L, 1L).getBody();
        assertEquals("SUCCESS", response.getMessage());
        verify(materialRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteMaterial_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        when(materialRepository.searchMaterialById(anyLong(), anyLong())).thenThrow(NullPointerException.class);

        doNothing().when(materialRepository).deleteById(anyLong()); 
        
        ResponseEntity<Object> responseEntity = materialService.deleteMaterial(1L, 1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteMaterial_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(materialRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = materialService.deleteMaterial(1L, 1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteMaterial_SectionNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.empty());
        doNothing().when(materialRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = materialService.deleteMaterial(1L, 1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteMaterial_MaterialNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(sectionRepository.searchSectionById(anyLong(), anyLong())).thenReturn(Optional.of(Section.builder().id(1L).build()));
        when(materialRepository.searchMaterialById(anyLong(), anyLong())).thenReturn(Optional.empty());
        doNothing().when(materialRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = materialService.deleteMaterial(1L, 1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());

    }
}
