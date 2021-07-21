package com.subject.genesislab.ctrl;

import com.subject.genesislab.dao.Board;
import com.subject.genesislab.dao.Video;
import com.subject.genesislab.dto.BoardDto;
import com.subject.genesislab.jwt.JwtFilter;
import com.subject.genesislab.jwt.TokenProvider;
import com.subject.genesislab.service.BoardService;
import com.subject.genesislab.util.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Optional;

@Controller
public class BoardController {

    private final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;
    private final TokenProvider tokenProvider;

    @Autowired
    public BoardController(BoardService boardService, TokenProvider tokenProvider){
        this.boardService = boardService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/board")
    public ModelAndView board(HttpServletRequest request, ModelAndView mv, @RequestParam(value = "id", required = false) String id){
        logger.debug(" ==> Call board method, params id : {}", id);

        // 쿠키에서 token 가져와서 본인인지 아닌지 확인, Admin은 프리패스
        Cookie cookie = CookieUtils.getCookie(request.getCookies(), JwtFilter.ACCESS_AUTHORIZATION_HEADER);
        Authentication authentication = tokenProvider.getAuthentication(cookie.getValue());
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch((authority) -> authority.equals("ROLE_ADMIN"));

        if(id == null){
            List<Board> boardList = boardService.doSelect();
            mv.addObject("board", boardList);
            mv.addObject("isAdmin", isAdmin);
            mv.setViewName("/board/board");
        } else {
            // 게시글 작성한 유저이거나 관리자인 경우 인가
            Board board = boardService.doSelect(id);
            if(board.getAuthor().equals(authentication.getName()) || isAdmin){
                Video video = boardService.findVideoByVideoId(board.getVideoFileId());
                logger.debug("board : {}, video : {}", board.toString(), video.toString());

                mv.addObject("board", board);
                mv.addObject("video", video);
                mv.setViewName("/board/content");
            } else {
                mv.addObject("title", "에러 페이지");
                mv.addObject("errorMessage", "해당 게시글을 볼 권한이 없습니다.");
                mv.setViewName("/error/errorPage");
            }
        }

        return mv;
    }

    @GetMapping("/post")
    public ModelAndView post(ModelAndView mv){
        mv.addObject("title", "글쓰기");
        mv.setViewName("/board/post");

        return mv;
    }

    @PostMapping("/post")
    public RedirectView post(HttpServletRequest request, BoardDto board){
        logger.debug(" ==> Call post method, params : {}", board.toString());
        Cookie cookie = CookieUtils.getCookie(request.getCookies(), JwtFilter.ACCESS_AUTHORIZATION_HEADER);
        // 토큰 받아서 getName하고 email 넣어주기
        boardService.createPost(board, cookie.getValue());
        // 게시판 메인으로 이동
        return new RedirectView("/board");
    }

    @GetMapping("/playVideo")
    public StreamingResponseBody stream(@RequestParam("path") String path) throws Exception {
        logger.debug(" ==> Call stream method, path : {}", path);

        File file = new File(path);
        final InputStream is = new FileInputStream(file);
        return os -> {
            readAndWrite(is, os);
        };
    }

    private void readAndWrite(final InputStream is, OutputStream os) throws IOException {
        byte[] data = new byte[2048];
        int read = 0;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);
        }
        os.flush();
    }
}
