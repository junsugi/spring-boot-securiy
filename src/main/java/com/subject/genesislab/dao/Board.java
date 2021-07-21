package com.subject.genesislab.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
@ToString
public class Board {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String summary;
    private String content;
    private String author;
    private String created;
    private long videoFileId;

    @Builder
    public Board(String summary, String content, String author, String created, long videoFileId){
        this.summary = summary;
        this.content = content;
        this.author = author;
        this.created = created;
        this.videoFileId = videoFileId;
    }
}
