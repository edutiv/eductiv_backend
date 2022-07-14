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
import com.projectlms.projectlms.domain.dto.CategoryDto;
import com.projectlms.projectlms.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CategoryService.class)
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Test
    void addCategory_Success_Test() {

        when(categoryRepository.save(any())).thenReturn(Category.builder()
            .id(1L)
            .categoryName("Backend Engineer")
            .categoryImage("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg")
            .description("Full-stack Web & Mobile Developer")
            .build()
        );

        ResponseEntity<Object> responseEntity = categoryService.addCategory(CategoryDto.builder()
            .categoryName("Backend Engineer")
            .build()
        );

        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Category category = (Category) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, category.getId());
        assertEquals("Backend Engineer", category.getCategoryName());
        assertEquals("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg", category.getCategoryImage());
        assertEquals("Full-stack Web & Mobile Developer", category.getDescription());
    }

    @Test
    void addCategory_Exception_Test(){
        when(categoryRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = categoryService.addCategory(CategoryDto.builder()
            .id(1L)
            .build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getAllCategories_Success_Test() {
        when(categoryRepository.findAll()).thenReturn(List.of(Category.builder()
            .id(1L)
            .categoryName("Backend Engineer")
            .categoryImage("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg")
            .description("Full-stack Web & Mobile Developer")
            .build()
        ));

        ResponseEntity<Object> responseEntity = categoryService.getAllCategories();
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        List<Category> categories = (List<Category>) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1, categories.size());
    }

    @Test
    void getAllCategories_Exception_Test() {
        when(categoryRepository.findAll()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = categoryService.getAllCategories();
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getCategoryDetail_Success_Test() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(Category.builder()
            .id(1L)
            .categoryName("Backend Engineer")
            .categoryImage("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg")
            .description("Full-stack Web & Mobile Developer")
            .build()
        ));

        ResponseEntity<Object> responseEntity = categoryService.getCategoryDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Category category = (Category) Objects.requireNonNull(response).getData();

        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1L, category.getId());
        assertEquals("Backend Engineer", category.getCategoryName());
        assertEquals("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg", category.getCategoryImage());
        assertEquals("Full-stack Web & Mobile Developer", category.getDescription());
    }

    @Test
    void getCategoryDetail_NotFound_Test() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = categoryService.getCategoryDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void getCategoryDetail_Exception_Test() {
        when(categoryRepository.findById(1L)).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = categoryService.getCategoryDetail(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateCategory_Success_Test() {
        Category category = Category.builder()
            .id(1L)
            .categoryName("Backend Engineer")
            .categoryImage("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg")
            .description("Full-stack Web & Mobile Developer")
            .build();
        
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(category);
        ResponseEntity<Object> responseEntity = categoryService.updateCategory(CategoryDto.builder()
            .categoryName("Backend Engineer")
            .categoryImage("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg")
            .description("Full-stack Web & Mobile Developer")
            .build(), 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        Category data = (Category) Objects.requireNonNull(response).getData();

        assertEquals(1L, data.getId());
        assertEquals("Backend Engineer", data.getCategoryName());
        assertEquals("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg", data.getCategoryImage());
        assertEquals("Full-stack Web & Mobile Developer", data.getDescription());
    }

    @Test
    void updateCategory_NotFound_Test() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = categoryService.updateCategory(CategoryDto.builder()
            .categoryName("Backend Engineer")
            .categoryImage("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg")
            .description("Full-stack Web & Mobile Developer")
            .build(), 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void updateCategory_Exception_Test() {
        Category category = Category.builder()
        .id(1L)
        .categoryName("Backend Engineer")
        .categoryImage("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg")
        .description("Full-stack Web & Mobile Developer")
        .build();
    
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = categoryService.updateCategory(CategoryDto.builder()
            .categoryName("Backend Engineer")
            .categoryImage("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg")
            .description("Full-stack Web & Mobile Developer")
            .build(), 1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteCategory_Success_Test() {
        when(categoryRepository.findById(anyLong()))
            .thenReturn(Optional.of(Category.builder()
                .id(1L)
                .categoryName("Backend Engineer")
                .categoryImage("https://i.pinimg.com/564x/47/c5/e9/47c5e94582e7f4e258b61f995d17e161.jpg")
                .description("Full-stack Web & Mobile Developer")
                .build()));
            doNothing().when(categoryRepository).deleteById(anyLong()); 
            
        ApiResponse response = (ApiResponse) categoryService.deleteCategory(1L).getBody();
        assertEquals("SUCCESS", response.getMessage());
        verify(categoryRepository, times(1)).deleteById(anyLong());
        
    }

    @Test
    void deleteCategory_NotFound_Test() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(categoryRepository).deleteById(anyLong()); 
        
        ResponseEntity<Object> responseEntity = categoryService.deleteCategory(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("DATA_NOT_FOUND", Objects.requireNonNull(response).getMessage());
    }

    @Test
    void deleteCategory_Exception_Test() {
        when(categoryRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        doNothing().when(categoryRepository).deleteById(anyLong()); 
        
        ResponseEntity<Object> responseEntity = categoryService.deleteCategory(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals("HAPPENED_ERROR", Objects.requireNonNull(response).getMessage());
    }
}
