package com.subject.genesislab.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "USER_ROLE")
public class UserRole implements Serializable {

    @Id
    @Column(name="ROLE_NAME")
    private String roleName;

    @Builder
    public UserRole(String roleName){
        this.roleName = roleName;
    }
}
