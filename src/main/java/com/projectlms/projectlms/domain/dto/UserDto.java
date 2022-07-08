package com.projectlms.projectlms.domain.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.projectlms.projectlms.domain.dao.RoleEnum;

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
    private String email;
    //private String username;
    private String password;
    private String profileImage;
    private Long specializationId;
    private List<RoleEnum> roles;
}
