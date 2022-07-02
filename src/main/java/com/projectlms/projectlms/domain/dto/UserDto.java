package com.projectlms.projectlms.domain.dto;

import java.io.Serializable;

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
public class UserDto implements Serializable {
    private static final long serialVersionUID =  1L;
    
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String role;
    private Long specializationId;
}
