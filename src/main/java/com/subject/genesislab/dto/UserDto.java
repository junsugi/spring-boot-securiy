package com.subject.genesislab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {

    private String userPw;
    private String userName;
    private String email;
    private String phoneNum;
    private String auth;
}
