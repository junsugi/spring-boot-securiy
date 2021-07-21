package com.subject.genesislab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenDto {

    private String accessJwt;
    private String refreshJwt;
}
