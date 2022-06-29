package com.projectlms.projectlms.domain.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// import java.util.Collection;
// import java.util.HashSet;
// import java.util.Set;
import java.util.List;
import javax.persistence.*;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "M_USER")
public class User extends BaseEntityWithDeletedAt {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", nullable =  false)
    private String firstname;

    @Column(name = "lastname", nullable =  false)
    private String lastname;

    @Column(name = "email", nullable =  false)
    private String email;

    @Column(name = "username", nullable =  false)
    private String username;

    @Column(name = "password", nullable =  false)
    private String password;

    // @Column(name = "role", nullable =  false)
    // private String role;

    @Column(name = "specialization", nullable =  false)
    private String specialization;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<EnrolledCourse> enrolledCourses;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Review> reviews;

    // @ManyToMany(fetch = FetchType.LAZY)
    // @JoinTable(
    //         name = "user_roles",
    //         joinColumns = @JoinColumn(name = "user_id"),
    //         inverseJoinColumns = @JoinColumn(name = "role_id")
    // )
    // private Set<Role> roles = new HashSet<>();

    // @Column(columnDefinition = "boolean default true")
    // private boolean active = true;

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {return null;}

    // @Override
    // public boolean isAccountNonExpired() {return active; }
    
    // @Override
    // public boolean isAccountNonLocked() {return active; }

    // @Override
    // public boolean isCredentialsNonExpired() {return active; }

    // @Override
    // public boolean isEnabled() {return active; }

    // public User(String firstname, String lastname, String email, String password, String role, String specialization) {
    //     this.firstname = firstname;
    //     this.lastname = lastname;
    //     this.email = email;
    //     this.password = password;
    //     this.role = role;
    //     this.specialization = specialization;
    // }

}
