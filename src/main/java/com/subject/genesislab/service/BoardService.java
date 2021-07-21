package com.subject.genesislab.service;

import com.subject.genesislab.dao.Board;
import com.subject.genesislab.dao.BoardRepository;
import com.subject.genesislab.dao.Video;
import com.subject.genesislab.dao.VideoRepository;
import com.subject.genesislab.dto.BoardDto;
import com.subject.genesislab.jwt.TokenProvider;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private final BoardRepository boardRepository;
    private final VideoRepository videoRepository;
    private final TokenProvider tokenProvider;

    @Autowired
    public BoardService(VideoRepository videoRepository, BoardRepository boardRepository, TokenProvider tokenProvider){
        this.videoRepository = videoRepository;
        this.boardRepository = boardRepository;
        this.tokenProvider = tokenProvider;
    }

    /*
        Board 테이블에 저장된 리스트 반환
     */
    public List<Board> doSelect() {
        List<Board> boardList = boardRepository.findAll();

        return boardList;
    }

    /*
        게시판 ID를 이용해서 해당 게시글의 정보를 반환
    */
    public Board doSelect(String id) {
        Optional<Board> boardOpt = boardRepository.findById(Long.parseLong(id));

        return boardOpt.orElse(null);
    }

    /*
        게시글 작성시 데이터베이스에 저장
     */
    public void createPost(BoardDto boardDto, String token){
        Authentication authentication = tokenProvider.getAuthentication(token);
        StringBuilder filePath = new StringBuilder();
        filePath.append("./videos");
        Path path = Paths.get(filePath.toString());
        try {
            // 폴더가 존재하는지 확인
            if (!Files.exists(path)) {
                File videoDir = new File(filePath.toString());
                videoDir.mkdir();
            }

            // 멀티파트를 이용해서 업로드된 비디오 파일 경로 지정해주기
            MultipartFile multipartFile = boardDto.getVideoFile();
            String fileName = multipartFile.getOriginalFilename();
            filePath.append("/" + fileName.replaceAll(" ", "_"));

            // videos 폴더 밑에 업로드된 비디오 파일 복사
            File videoFile = new File(filePath.toString());
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, videoFile);

            // 디비 저장 일자가 생성일
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String created = format.format(new Date());

            // 디비 저장 하기
            videoRepository.save(Video.builder()
                           .fileName(fileName)
                           .filePath(filePath.toString())
                           .build());

            long fileId = videoRepository.findAll().get(videoRepository.findAll().size() - 1).getId();

            // 게시판 저장 하기
            boardRepository.save(Board.builder()
                           .summary(boardDto.getSummary())
                           .author(authentication.getName())
                           .content(boardDto.getContent())
                           .videoFileId(fileId)
                           .created(created)
                           .build());

        } catch(IOException e){
            logger.error(e.getMessage(), e);
        }
    }

    /*
        해당 게시글과 연결된 비디오 ID로 비디오 경로를 찾아서 반환
    */
    public Video findVideoByVideoId(long id) {
        Optional<Video> videoOpt = videoRepository.findById(id);

        return videoOpt.orElseGet(null);
    }
}