package com.example.shop.configuration;

import com.example.shop.builder.ErrorMessageBuilder;
import com.example.shop.builder.ErrorMessageBuilderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    String uploadPath;

    //정적 리소스 설정
    // "/images/**" 로 요청되는 모든 url 이 uploadPath 에 있는 정적 리소스로 매핑됨
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }

    @Bean
    public ErrorMessageBuilder errorMessageBuilder() {
        return new ErrorMessageBuilderImpl();
    }
}
