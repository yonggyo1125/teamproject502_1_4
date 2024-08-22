package org.hidog.global.configs;

import lombok.RequiredArgsConstructor;
import org.hidog.global.interceptors.CommonInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    //사이트 공통 인터셉터
    private final CommonInterceptor commonInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor);
        //인터셉터추가 (.addPathPatterns("/**")) 생략가능. 모든컨트롤러에 적용.
        //특정 부분만 추가 하고싶으면 패턴경로 설정하면됨.
    }
}
