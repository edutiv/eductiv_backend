package com.projectlms.projectlms.domain.dto;

import java.io.Serializable;

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
public class MaterialDto implements Serializable{
    private static final long serialVersionUID =  1L;
    
    private Long id;
    private Long sectionId;
    private Long courseId;
    private String materialType;
    private String materialName;
    private String materialUrl;
    private Boolean isComplete;
}
