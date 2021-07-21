package com.subject.genesislab.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "USER_TOKEN")
public class UserToken implements Serializable {

    @Id
    private String email;
    private String refreshToken;

    @Builder
    public UserToken(String email, String refreshToken){
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
