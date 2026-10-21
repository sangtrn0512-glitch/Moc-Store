package org.example.asm_java5.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
  private final AuthInterceptor interceptor;

  public AuthConfig(AuthInterceptor interceptor) {
    this.interceptor = interceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(interceptor)
        .excludePathPatterns(
            "/css/**", "/js/**", "/images/**", "/uploads/**", "/error", "/favicon.ico");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/images/**")
        .addResourceLocations(
            java.nio.file.Path.of("uploads").toAbsolutePath().toUri().toString(),
            "classpath:/static/images/");
    registry
        .addResourceHandler("/uploads/**")
        .addResourceLocations(java.nio.file.Path.of("uploads").toAbsolutePath().toUri().toString());
  }
}
