package com.subject.genesislab.ctrl;

import com.subject.genesislab.config.SecurityConfig;
import com.subject.genesislab.dao.User;
import com.subject.genesislab.jwt.*;
import com.subject.genesislab.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(
        classes = {
                TokenProvider.class,
                SecurityConfig.class,
                CorsFilter.class,
                JwtAuthenticationEntryPoint.class,
                JwtAccessDeniedHandler.class,
                JwtFilter.class,
                JwtSecurityConfig.class
        }
)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @InjectMocks
    private UserController userController;

    @Before
    public void before(){
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @DisplayName("회원가입 페이지가 뜨는지 확인하는 테스트")
    @Test
    public void signUpPageTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/signUp"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("회원가입이 정상적으로 되는지 확인하는 테스트")
    @Test
    public void signUpTest() throws Exception{

        User user = User.builder()
                .signDate("2021-07-19")
                .phoneNum("01099998888")
                .userPw("$2a$10$wesf1N2Lbme8eokPsJoDEeZ0nJ.JbJQxuCYvmPsfUmESHbUIPs6gK")
                .userName("test1")
                .email("test1@test.com")
                .build();

        mockMvc.perform(post("/signUp")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity(
                        "userPw", user.getUserPw(),
                        "email", user.getEmail(),
                        "userName", user.getUserName(),
                        "phoneNum", user.getPhoneNum()
                )))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @DisplayName("회원정보 수정 페이지 접근 테스트")
    @Test
    public void profilePageTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("회원정보 수정이 정상적으로 되는지 테스트")
    @Test
    public void profileTest() throws Exception {
        User user = User.builder()
                .signDate("2021-07-19")
                .phoneNum("01099998888")
                .userName("test1")
                .email("test1@test.com")
                .build();

        mockMvc.perform(post("/profile")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity(
                        "email", user.getEmail(),
                        "userName", user.getUserName(),
                        "phoneNum", user.getPhoneNum()
                )))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @DisplayName("로그아웃 테스트 (미완성)")
    @Test
    public void logoutTest() throws Exception{
        mockMvc.perform(delete("/user")
                .param("email", "test@test.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().equals("success");
    }

    @Test
    public void userStatPageTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/user/stat"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /*
        Form 데이터 보낼 때 사용
     */
    private String buildUrlEncodedFormEntity(String... params) {
        if( (params.length % 2) > 0 ) {
            throw new IllegalArgumentException("Need to give an even number of parameters");
        }
        StringBuilder result = new StringBuilder();
        for (int i=0; i<params.length; i+=2) {
            if( i > 0 ) {
                result.append('&');
            }
            try {
                result.
                        append(URLEncoder.encode(params[i], StandardCharsets.UTF_8.name())).
                        append('=').
                        append(URLEncoder.encode(params[i+1], StandardCharsets.UTF_8.name()));
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return result.toString();
    }
}
