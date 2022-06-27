package com.projectlms.projectlms.domain.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "M_ROLES")
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue()
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column
    private RoleEnum name;
}
