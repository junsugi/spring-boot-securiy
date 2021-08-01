package com.subject.genesislab.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BoardDto {

    private final String summary;
    private String content;
    private final String author;
    private final String created;
    private MultipartFile videoFile;
}
