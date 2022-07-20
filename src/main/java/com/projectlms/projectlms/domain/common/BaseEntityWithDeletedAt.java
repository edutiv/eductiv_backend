package com.projectlms.projectlms.domain.common;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class BaseEntityWithDeletedAt extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @PrePersist
    void onCreate(){
        this.isDeleted = Boolean.FALSE;
        this.setCreatedAt(LocalDateTime.now());
    }
}
