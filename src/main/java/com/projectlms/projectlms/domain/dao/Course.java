package com.projectlms.projectlms.domain.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "M_COURSE")
public class Course extends BaseEntityWithDeletedAt {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", nullable = false)
    private String courseName; 

    @Column(name = "course_image", nullable = false)
    private String courseImage; 

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "description", nullable = false)
    private String description; 

    @Column(name = "total_video", nullable = false)
    private Integer totalVideo;

    @Column(name = "total_times", nullable = false)
    private String totalTimes;
    
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course")
    private List<Section> sections;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course")
    private List<EnrolledCourse> enrolledCourses;

    // @JsonIgnore
    // @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course")
    // private List<Review> reviews;

    // @JsonIgnore
    // @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course")
    // private List<Tool> tools;
}
