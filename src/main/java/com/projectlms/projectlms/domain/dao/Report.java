package com.projectlms.projectlms.domain.dao;

import com.projectlms.projectlms.domain.common.BaseEntityWithDeletedAt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "M_REPORT")
@SQLDelete(sql = "UPDATE M_REPORT SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
public class Report extends BaseEntityWithDeletedAt{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private Boolean isCompleted = false;

    @ManyToOne
    @JoinColumn(name = "enrolled_course_id")
    @JsonBackReference
    private EnrolledCourse enrolledCourse;

    @ManyToOne
    @JoinColumn(name = "material_id")
    @JsonManagedReference
    private Material material;
    
}
