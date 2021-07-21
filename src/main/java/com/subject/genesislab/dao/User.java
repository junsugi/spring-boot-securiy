package com.subject.genesislab.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name="EMAIL")
    private String email;
    @Column(nullable = false)
    private String userPw;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String phoneNum;
    @Column(nullable = false)
    private String signDate;
    @ManyToMany
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "EMAIL", referencedColumnName = "EMAIL")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_NAME", referencedColumnName = "ROLE_NAME")})
    private Set<UserRole> authorities;

}
