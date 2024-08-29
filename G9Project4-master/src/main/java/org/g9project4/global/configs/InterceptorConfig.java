package org.g9project4.global.configs;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.interceptors.CommonInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    // 사이트 공통 인터셉터
    private final CommonInterceptor commonInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 공통 적용
        registry.addInterceptor(commonInterceptor);
        //.addPathPatterns("/**"); 모든페이지 생략 가능
    }
}
