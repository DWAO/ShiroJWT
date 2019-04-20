package com.zorpz.zcoe.shiro.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Punk
 * @date 2019/04/10
 */
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(length = 32)
    private String id;

    @Column(length = 32, unique = true, nullable = false)
    private String username;

    @Column(length = 32, nullable = false)
    private String password;

    @Column(length = 32, nullable = true)
    private String salt;

    private Boolean disabled;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;




}
