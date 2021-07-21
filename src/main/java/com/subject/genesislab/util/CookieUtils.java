package com.subject.genesislab.util;

import javax.servlet.http.Cookie;

public class CookieUtils {

    public static Cookie createCookie(String cookieName, String value, long expiredTime){
        Cookie token = new Cookie(cookieName, value);
        token.setHttpOnly(true);
        token.setMaxAge((int) expiredTime);
        token.setPath("/");
        return token;
    }

    public static Cookie getCookie(Cookie[] cookies, String cookieName){
        if(cookies == null)
            return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }
}
