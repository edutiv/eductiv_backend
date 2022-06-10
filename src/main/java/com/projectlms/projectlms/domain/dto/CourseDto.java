package com.projectlms.projectlms.domain.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto implements Serializable {
    private static final long serialVersionUID =  1L;
    
    private Long id;
    private String courseName;
    private Long categoryId;
    private String description;
    private Integer rating;
    
}
