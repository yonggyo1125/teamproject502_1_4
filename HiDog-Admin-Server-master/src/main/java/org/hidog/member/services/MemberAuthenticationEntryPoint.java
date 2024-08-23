package org.hidog.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        /**
         * 회원 전용 페이지로 접근한 경우 - /mypage-> 로그인 페이지 이동
         * 관리자 페이지로 접근한 경우 - 응답 코드 401, 에러페이지 출력
         * */
        String uri = request.getRequestURI();
        if (uri.contains("/admin")) { //관리자 페이지
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {//회원 전용 페이지
            String qs = request.getQueryString();
            String redirectUrl = uri.replace(request.getContextPath(), "");
            redirectUrl = StringUtils.hasText(qs) ? redirectUrl + "?" + qs : redirectUrl;
            response.sendRedirect(request.getContextPath() + "/member/login?redirectUrl=" + redirectUrl);
        }
    }
}
