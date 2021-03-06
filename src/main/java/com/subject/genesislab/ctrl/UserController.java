package com.subject.genesislab.ctrl;

import com.subject.genesislab.dao.User;
import com.subject.genesislab.dto.UserDto;
import com.subject.genesislab.jwt.JwtFilter;
import com.subject.genesislab.jwt.TokenProvider;
import com.subject.genesislab.service.UserService;
import com.subject.genesislab.util.CookieUtils;
import javassist.bytecode.DuplicateMemberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public UserController(UserService userService, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder){
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    /*
        ?????? ?????? ?????? ??????
     */
    @GetMapping("/signUp")
    public ModelAndView signUpPage(ModelAndView mv){
        logger.debug(" ==> call signUpPage method");
        mv.addObject("title", "???????????? ?????????");
        mv.setViewName("/user/signUp");

        return mv;
    }

    /*
        ?????? ??????
     */
    @PostMapping("/signUp")
    public Object signUp(ModelAndView mv, UserDto userDto){
        logger.debug(" ==> Call signUp method, params : {}", userDto);
        try{
            userService.signUp(userDto);
            return new RedirectView("/index");
        } catch (DuplicateMemberException e){
            mv.addObject("title", "?????? ?????????");
            mv.addObject("errorMessage", e.getMessage());
            mv.setViewName("/error/errorPage");
        }

        return mv;
    }

    /*
        ?????? ?????? ?????? ?????? ??????
     */
    @GetMapping("/profile")
    public ModelAndView profilePage(ModelAndView mv, HttpServletRequest request){
        Cookie cookie = CookieUtils.getCookie(request.getCookies(), JwtFilter.ACCESS_AUTHORIZATION_HEADER);
        User user = userService.findUserByAccessToken(cookie);

        mv.addObject("title", "???????????? ??????");
        mv.addObject("user", user);
        mv.setViewName("/user/profile");

        return mv;
    }

    /*
        ?????? ?????? ??????
     */
    @PostMapping("/profile")
    public Object profile(ModelAndView mv, UserDto userDto){
        logger.debug(" ==> Call profile method, params : {}", userDto.toString());
        try{
            userService.updateProfile(userDto);
        } catch (NoSuchElementException e){
            mv.addObject("title", "?????? ?????????");
            mv.addObject("errorMessage", e.getMessage());
            mv.setViewName("/error/errorPage");
            return mv;
        }

        return new RedirectView("/board");
    }

    /*
        ??????????????? ?????? ??????
     */
    @DeleteMapping("/user")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@RequestParam("email") String email, HttpServletRequest request, HttpServletResponse response){
        logger.debug(" ==> Call deleteUser, params : {}", email);
        userService.deleteUser(email);
        // ?????? ???????????? ?????? ???????????????
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies){
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return ResponseEntity.ok("success");
    }

    /*
        ??????????????? ?????? ??????
     */
    @ResponseBody
    @GetMapping("/loggedout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        logger.debug(" ==> Call logout method");
        for(Cookie cookie: request.getCookies()){
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return ResponseEntity.ok("success");
    }

    /*
        ????????? ?????? ?????? ??? ?????? ??????
     */
    @PostMapping("/login/auth")
    public Object loginAuth(ModelAndView mv, HttpServletResponse response, UserDto userDto){
        logger.debug(" ==> Call loginAuth method !, params : {}", userDto.toString());
        try{
            // ?????? ???????????? ??????????????? ????????? ????????? ?????????.
            // ????????? ????????? Authentication Manager??? ????????? ????????????.
            // ????????? ???????????? Authentication??? ????????????.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getUserPw());

            // ????????? ????????? ????????? ?????? ??????????????? (UserDetailService??? ???????????? ??????????????? ??????)
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // ????????? ???????????? ?????? ???????????? ???????????? ??????, SecurityContext??? ?????? Authentication ????????? ??? ??????
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ??????????????? ??? ???????????? ?????? ??????
            String accessToken = tokenProvider.createToken(authentication, TokenProvider.ACCESS_TOKEN_EXP_TIME);
            String refreshToken = tokenProvider.createToken(authentication, TokenProvider.REFRESH_TOKEN_EXP_TIME);

            Cookie cAccessToken = CookieUtils.createCookie(JwtFilter.ACCESS_AUTHORIZATION_HEADER, accessToken, TokenProvider.ACCESS_TOKEN_EXP_TIME);
            Cookie cRefreshToken = CookieUtils.createCookie(JwtFilter.REFRESH_AUTHORIZATION_HEADER, refreshToken, TokenProvider.REFRESH_TOKEN_EXP_TIME);
            response.addCookie(cAccessToken);
            response.addCookie(cRefreshToken);

        } catch (BadCredentialsException e){
            // ????????? ?????? ??????????????? ???????????? ????????????????????? ????????? ???????????? ?????? ??????
            logger.error(e.getMessage(), e);
            mv.addObject("title", "?????? ?????????");
            mv.addObject("errorMessage", "????????? ?????? ??????????????? ????????????.");
            mv.setViewName("/error/errorPage");
            return mv;
        }

        return new RedirectView("/board");
    }


    /*
       ?????? ?????? REST API (Admin??? ?????? ??????)
     */
    @GetMapping("/user/stat")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ModelAndView getUserStatPage(ModelAndView mv){
        mv.addObject("title", "?????? ??????");
        mv.setViewName("/statistics/userStat");
        return mv;
    }


    @GetMapping("/api/user/stat")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponseEntity<?> searchSignUpUser(@RequestParam("startDate") String startDate,
                                              @RequestParam(value = "endDate", required = false) String endDate){

        logger.debug(" ==> Call searchUserByStartAndEnd, params : {} ~ {}", startDate, endDate != null ? endDate : startDate);
        List<Object[]> userList = userService.findUserByDate(startDate, endDate != null ? endDate : startDate);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("startDate", startDate);
        resultMap.put("endDate", endDate != null ? endDate : startDate);
        resultMap.put("userList", userList);

        return ResponseEntity.ok(resultMap);
    }
}

