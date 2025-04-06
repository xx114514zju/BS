package com.zju.mqtt.Configer;

import com.zju.mqtt.Filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebMvcConfig {
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtFilter);
        // 添加多个 URL 模式，同时拦截 /mountFirst、/mountSecond 和 /mountThird 接口
        registration.addUrlPatterns("/mountFirst", "/mountSecond", "/mountThird", "/getUserDevice","/getMessage","/getPoints","/editDevice","/Adder", "/delDevice","/resetUsername","/resetemail");
        return registration;
    }
}