package com.computer.parts.shop.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConf implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
      .addMapping("/**")
      .allowedOrigins("http://localhost:8080/")
      .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS")
      .allowCredentials(true)
      .allowedHeaders("*");
  }
}
