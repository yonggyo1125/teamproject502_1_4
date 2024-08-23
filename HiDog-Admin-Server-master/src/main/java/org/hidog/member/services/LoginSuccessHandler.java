package org.hidog.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.hidog.global.Utils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Setter
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private Utils utils;

   // public void setUtils(Utils utils){
   //     this.utils = utils;
  //  }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();

        //세션에 남아있는 requestLogin 값 제거
        session.removeAttribute("requestLogin");
        //로그인 성공시 - redirectUrl 이 있으면 해당 주소로 이동, 아니면 메인 페이지 이동
        String redirectUrl = StringUtils.hasText(request.getParameter("redirectUrl")) ? (request.getParameter("redirectUrl").trim()) : "";


        response.sendRedirect(utils.redirectUrl(redirectUrl));
    }
}
