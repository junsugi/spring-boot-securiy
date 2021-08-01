package com.subject.genesislab.dto;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserDto {

    private final String userPw;
    private final String userName;
    private final String email;
    private final String phoneNum;
    private final String auth;
    private final String signDate;
}
