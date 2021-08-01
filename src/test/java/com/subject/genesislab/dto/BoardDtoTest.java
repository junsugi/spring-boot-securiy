package com.subject.genesislab.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardDtoTest {

    @Test
    @DisplayName("DTO 생성 테스트")
    public void createTest(){
        String summary = "테스트";
        String author = "이준석";
        String created = new Date().toString();

        BoardDto boardDto = new BoardDto(summary, author, created);
        assertThat(boardDto.getSummary()).isEqualTo(summary);
        assertThat(boardDto.getAuthor()).isEqualTo(author);
        assertThat(boardDto.getCreated()).isEqualTo(created);
    }
}
