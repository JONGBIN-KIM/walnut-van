package com.assignment.walnut.config;

import com.assignment.walnut.interceptor.WhitelistInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final WhitelistInterceptor whitelistInterceptor;

    public WebMvcConfig(WhitelistInterceptor whitelistInterceptor) {
        this.whitelistInterceptor = whitelistInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(whitelistInterceptor)
                .addPathPatterns("/api/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/logs")
                .allowedOrigins("http://localhost:8443") // PG 시스템 주소
                .allowedMethods("GET");
    }
}
