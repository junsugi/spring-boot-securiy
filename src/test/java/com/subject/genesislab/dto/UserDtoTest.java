package com.subject.genesislab.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDtoTest {

    @Test
    @DisplayName("USER DTO 생성 테스트")
    public void createTest(){
        String userPw = "test";
        String email = "wnstjrl96@naver.com";
        String phoneNum = "010000000000";
        String auth = "ROLE_USER";
        String userName = "이준석";
        String signDate = new Date().toString();

        UserDto userDto = new UserDto(userPw, userName, email, phoneNum, auth, signDate);
        assertThat(userDto.getUserPw()).isEqualTo(userPw);
        assertThat(userDto.getUserName()).isEqualTo(userName);
        assertThat(userDto.getEmail()).isEqualTo(email);
        assertThat(userDto.getPhoneNum()).isEqualTo(phoneNum);
        assertThat(userDto.getAuth()).isEqualTo(auth);
        assertThat(userDto.getSignDate()).isEqualTo(signDate);
    }
}
