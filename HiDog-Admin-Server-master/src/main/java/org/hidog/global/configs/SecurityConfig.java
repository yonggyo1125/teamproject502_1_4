package org.hidog.global.configs;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hidog.global.Utils;
import org.hidog.member.services.LoginFailureHandler;
import org.hidog.member.services.LoginSuccessHandler;
import org.hidog.member.services.MemberAuthenticationEntryPoint;
import org.hidog.member.services.MemberInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberInfoService memberInfoService;
    private final Utils utils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LoginSuccessHandler loginSuccessHandler = new LoginSuccessHandler();
        LoginFailureHandler loginFailureHandler = new LoginFailureHandler();
        loginSuccessHandler.setUtils(utils);
        loginFailureHandler.setUtils(utils);

        /* 로그인, 로그아웃 S */
        http.formLogin(f -> {
            f.loginPage("/member/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler);
        });

        http.logout(logout -> {
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessHandler((req, res, e) -> {

                        HttpSession session = req.getSession();
                        session.removeAttribute("device");

                        res.sendRedirect(req.getContextPath() + utils.redirectUrl("/member/login"));
                    });
        });
        /* 로그인, 로그아웃 E */
        /* 인가(접근 통제) 설정 S*/
        http.authorizeRequests(authorizeRequests -> {
            authorizeRequests.requestMatchers("/mypage/**").authenticated()//회원 전용
                    .anyRequest().permitAll();
        });
        http.exceptionHandling(c -> {
            c.authenticationEntryPoint(new MemberAuthenticationEntryPoint())//예외

                    .accessDeniedHandler((req, res, e) -> {
                        res.sendError(HttpStatus.FORBIDDEN.value());
                    });
        });
        /* 인가(접근 통제) 설정 E*/
        //iframe 자원 출처를 같은 서버 자원으로 한정
        http.headers(c -> c.frameOptions(f -> f.sameOrigin()));

        /*자동 로그인 설정 S*/
        http.rememberMe(c-> {
            c.rememberMeParameter("autoLogin")
                    .tokenValiditySeconds(60*60*24*15) // 15일간 유효
                    .userDetailsService(memberInfoService) //재로그인할 때 인증을 위해
                    .authenticationSuccessHandler(loginSuccessHandler); // 자동 로그인 성공-> handler가 처리
        });
        /*자동 로그인 설정 E*/

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/payment/**");
    }
}