package com.example.rhythme_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","http://3.34.3.250","http://treavelrecommend.s3-website-us-east-1.amazonaws.com")
                .allowedMethods("*")
                .exposedHeaders("Authorization","Refresh-Token")
                .allowCredentials(true)//make client read header("jwt-token")
        ;
    }
}
