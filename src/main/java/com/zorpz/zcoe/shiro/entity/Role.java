package com.zorpz.zcoe.shiro.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Punk
 * @date 2019/04/10
 */
@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(length = 32)
    private String id;

    @Column(length = 32, unique = true, nullable = false)
    private String code;

    @Column(length = 32, unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission_test", joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> permissionList = new ArrayList<>();
}
