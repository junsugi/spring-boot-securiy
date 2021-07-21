package com.subject.genesislab.jwt;

import com.subject.genesislab.util.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String ACCESS_AUTHORIZATION_HEADER = "JWT-ACCESS-TOKEN";
    public static final String REFRESH_AUTHORIZATION_HEADER = "JWT-REFRESH-TOKEN";

    private final TokenProvider tokenProvider;

    @Autowired
    public JwtFilter(TokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        String jwt = resolveToken(httpServletRequest);

        // 나중에 리프레시 토큰으로 엑세스 토큰 재발급 하는 부분 추가하기.
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, token : {}, uri: {}", jwt, requestURI);
        }


        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        StringBuilder bearerToken = new StringBuilder();
        Cookie[] cookies = request.getCookies();
        if( cookies != null ){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals(ACCESS_AUTHORIZATION_HEADER)){
                    bearerToken.append("Bearer ")
                               .append(cookie.getValue());
                    break;
                }
            }
        }
        if (StringUtils.hasText(bearerToken.toString()) && bearerToken.toString().startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
