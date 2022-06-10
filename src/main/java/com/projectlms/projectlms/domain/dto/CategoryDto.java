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
public class CategoryDto implements Serializable {
    private static final long serialVersionUID =  1L;
    
    private Long id;
    private String category;
    private String description;
}
