package web.video.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import web.video.interceoter.LoginInterceoter;


/**
 * 拦截器配置
 */
@Configuration
public class IntercepterConfig implements WebMvcConfigurer{


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceoter()).addPathPatterns("/user/*/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
