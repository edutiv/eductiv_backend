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
import com.projectlms.projectlms.domain.dao.Course;
import com.projectlms.projectlms.domain.dao.Tool;
import com.projectlms.projectlms.domain.dto.ToolDto;
import com.projectlms.projectlms.repository.CourseRepository;
import com.projectlms.projectlms.repository.ToolRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ToolService.class)
public class ToolServiceTest {

    @MockBean
    private ToolRepository toolRepository;

    @MockBean 
    private CourseRepository courseRepository;

    @Autowired
    private ToolService toolService;

    @Test
    void addTool_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        
        when(toolRepository.save(any())).thenReturn(Tool.builder()
                .id(1L)
                .toolName("Vue Devtools")
                .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
                .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
                .course(Course.builder().id(1L).build())
                .build());

        ResponseEntity<Object> responseEntity = toolService.addTool(ToolDto.builder()
                .toolName("Vue Devtools")
                .build()
        );

        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Tool tool = (Tool) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, tool.getId());
        assertEquals("Vue Devtools", tool.getToolName());
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png", tool.getToolIcon());
        assertEquals("https://v2.vuejs.org/v2/guide/installation.html", tool.getToolUrl());
    }

    @Test
    void addTool_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));

        when(toolRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = toolService.addTool(ToolDto.builder().id(1L).build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void addRequest_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = toolService.addTool(ToolDto.builder()
            .id(1L)
            .toolName("Vue Devtools")
            .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
            .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
            .courseId(1L)
            .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND",Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllTool_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));

        when(toolRepository.searchAllTools(anyLong())).thenReturn(List.of(Tool.builder()
                .id(1L)
                .toolName("Vue Devtools")
                .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
                .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
                .course(Course.builder().id(1L).build())
                .build()
        ));
        ResponseEntity<Object> responseEntity = toolService.getAllTool(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        List<Tool> tools = (List<Tool>) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(1,tools.size());
    }

    @Test
    void getAllTool_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(toolRepository.searchAllTools(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = toolService.getAllTool(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllTool_NotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        when(toolRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = toolService.getAllTool(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllTool_ToolNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(toolRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = toolService.getAllTool(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getToolDetail_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(toolRepository.searchToolById(anyLong(), anyLong())).thenReturn(Optional.of(Tool.builder()
                .id(1L)
                .toolName("Vue Devtools")
                .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
                .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
                .course(Course.builder().id(1L).build())
                .build()
        ));

        ResponseEntity<Object> responseEntity = toolService.getToolDetail(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Tool tool = (Tool) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, tool.getId());
        assertEquals("Vue Devtools", tool.getToolName());
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png", tool.getToolIcon());
        assertEquals("https://v2.vuejs.org/v2/guide/installation.html", tool.getToolUrl());
    }

    @Test
    void getToolDetail_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = toolService.getToolDetail(1L,1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getToolDetail_ToolNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(toolRepository.searchToolById(anyLong(), anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = toolService.getToolDetail(1L,1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getToolDetail_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(toolRepository.searchToolById(1L, 1L)).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = toolService.getToolDetail(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateTool_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        Tool tool = Tool.builder()
                .id(1L)
                .toolName("Vue Devtools")
                .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
                .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
                .course(Course.builder().id(1L).build())
                .build();
        when(toolRepository.searchToolById(anyLong(), anyLong())).thenReturn(Optional.of(tool));
        when(toolRepository.save(any())).thenReturn(tool);
        ResponseEntity<Object> responseEntity = toolService.updateTool(1L, ToolDto.builder()
                .toolName("Vue Devtools")
                .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
                .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
                .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Tool data = (Tool) Objects.requireNonNull(response).getData();

        assertEquals(1L, data.getId());
        assertEquals("Vue Devtools", data.getToolName());
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png", data.getToolIcon());
        assertEquals("https://v2.vuejs.org/v2/guide/installation.html", data.getToolUrl());
    }

    @Test
    void updateTool_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = toolService.updateTool(1L, ToolDto.builder()
                .toolName("Vue Devtools")
                .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
                .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
                .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateTool_ToolNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(toolRepository.searchToolById(anyLong(), anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = toolService.updateTool(1L, ToolDto.builder()
                .toolName("Vue Devtools")
                .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
                .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
                .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateTool_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        Tool tool = Tool.builder()
                .id(1L)
                .toolName("Vue Devtools")
                .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
                .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
                .course(Course.builder().id(1L).build())
                .build();

        when(toolRepository.searchToolById(anyLong(), anyLong())).thenReturn(Optional.of(tool));
        when(toolRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = toolService.updateTool(1L, ToolDto.builder()
                .toolName("Vue Devtools")
                .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
                .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
                .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteTool_Success_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(toolRepository.searchToolById(anyLong(), anyLong()))
                .thenReturn(Optional.of(Tool.builder()
                        .id(1L)
                        .toolName("Vue Devtools")
                        .toolIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1200px-Vue.js_Logo_2.svg.png")
                        .toolUrl("https://v2.vuejs.org/v2/guide/installation.html")
                        .course(Course.builder().id(1L).build())
                        .build()));
        doNothing().when(toolRepository).deleteById(anyLong());

        ApiResponse response = (ApiResponse) toolService.deleteTool(1L, 1L).getBody();
        assertEquals("SUCCESS", response.getMessage());
        verify(toolRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteTool_CourseNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(toolRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = toolService.deleteTool(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteTool_ToolNotFound_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(toolRepository.searchToolById(anyLong(), anyLong())).thenReturn(Optional.empty());
        doNothing().when(toolRepository).deleteById(anyLong());

        ResponseEntity<Object> responseEntity = toolService.deleteTool(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }
 
    @Test
    void deleteTool_Exception_Test() {
        when(courseRepository.searchCourseById(anyLong())).thenReturn(Optional.of(Course.builder().id(1L).build()));
        when(toolRepository.searchToolById(anyLong(), anyLong())).thenThrow(NullPointerException.class);
        doNothing().when(toolRepository).deleteById(anyLong()); 
        
        ResponseEntity<Object> responseEntity = toolService.deleteTool(1L, 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }
}
