package com.projectlms.projectlms.domain.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "M_ENROLLED_COURSE")
@SQLDelete(sql = "UPDATE M_ENROLLED_COURSE SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
public class EnrolledCourse extends BaseEntityWithDeletedAt{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    @Min(value = 0) @Max(value = 5)
    private Double rating;

    @Column(name = "progress")
    @Builder.Default
    private Integer progress = 0;

    @Column(name = "review")
    private String review;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    //@JsonBackReference
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonManagedReference
    //@JsonBackReference
    private Course course;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "enrolledCourse")
    @JsonManagedReference
    private List<Report> reports;
}
