package com.subject.genesislab.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class Video {

    @Id
    @GeneratedValue
    private long id;
    private String fileName;
    private String filePath;

    @Builder
    public Video(String fileName, String filePath){
        this.fileName = fileName;
        this.filePath = filePath;
    }

}
