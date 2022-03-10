package com.example.shop.security.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof BadCredentialsException) {
            //인코딩을 하지 않으면 ASCII 로 보내기 때문에 utf-8로 인코딩 필요
            String errorMessage = URLEncoder.encode("아이디가 존재하지 않거나 패스워드가 일치하지 않습니다.", "utf-8");
            setDefaultFailureUrl("/member/login?loginException=" + errorMessage);
        } else {
            setDefaultFailureUrl("/member/login");
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
