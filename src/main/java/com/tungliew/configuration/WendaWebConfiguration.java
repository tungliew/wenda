package com.tungliew.configuration;

import com.tungliew.interceptor.LoginRequiredInterceptor;
import com.tungliew.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Component //拦截器部署到服务器
public class WendaWebConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor); //系统初始化的时候增加自己的拦截器
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/users/*");//类似"users/*"之类的页面才使用这个拦截器
        super.addInterceptors(registry);
    }
}