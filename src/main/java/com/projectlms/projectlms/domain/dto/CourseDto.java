package com.projectlms.projectlms.domain.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDto implements Serializable {
    private static final long serialVersionUID =  1L;
    
    private Long id;
    private String courseName;
    private String courseImage;
    private Long categoryId;
    private String description;
    private List<String> learningObjectives;
    private List<String> advantages;
    private Integer totalVideo;
    private String totalTimes;
}
