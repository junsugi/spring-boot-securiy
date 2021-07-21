package com.subject.genesislab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class BoardDto {

    private String summary;
    private String content;
    private String author;
    private String created;
    private MultipartFile videoFile;
}
