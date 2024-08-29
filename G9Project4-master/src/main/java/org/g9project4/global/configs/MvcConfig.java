package org.g9project4.global.configs;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableScheduling
public class MvcConfig implements WebMvcConfigurer {
    /*
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("front/index");
    }
    */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/**")
               .addResourceLocations("classpath:/static/");
    }

    /*
    * <input type="hidden" name="_method" value="PATCH">->patch 방식으로 요청
    * ?_method=DELETE
    * */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {//_method 방식으로 보냈을 때 해당 방식으로 처리
        return new HiddenHttpMethodFilter();
    }
}
