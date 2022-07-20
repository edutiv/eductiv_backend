package com.projectlms.projectlms.domain.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import javax.persistence.*;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "M_COURSE")
@SQLDelete(sql = "UPDATE M_COURSE SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
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

    // @ManyToOne
    // @JoinColumn(name = "mentor_id")
    // private User user;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; 

    @ElementCollection
    @CollectionTable(name = "learning_objectives")
    @Column(columnDefinition = "TEXT")
    private List<String> learningObjectives;

    @ElementCollection
    @CollectionTable(name = "advantages")
    @Column(columnDefinition = "TEXT")
    private List<String> advantages;

    @Column(name = "total_rating")
    @Builder.Default
    private Double totalRating = 0.0;
    @Column(name = "total_video")
    private Integer totalVideo;

    @Column(name = "total_times")
    private String totalTimes;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course")
    @JsonManagedReference
    private List<Section> sections;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course")
    @JsonBackReference
    //@JsonManagedReference
    private List<EnrolledCourse> enrolledCourses;

    // @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course")
    // @JsonManagedReference
    // private List<Review> reviews;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course")
    @JsonManagedReference
    private List<Tool> tools;
}
