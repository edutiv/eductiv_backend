package com.projectlms.projectlms.domain.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.projectlms.projectlms.domain.common.BaseEntityWithDeletedAt;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "M_SLIDE")
public class Slide extends BaseEntityWithDeletedAt{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slide_name", nullable = false)
    private String slideName; 

    @Column(name = "slide_url", nullable = false)
    private String slideUrl; 

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;
}
