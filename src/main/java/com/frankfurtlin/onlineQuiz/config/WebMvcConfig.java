package com.frankfurtlin.onlineQuiz.config;

import com.frankfurtlin.onlineQuiz.component.LoginHanderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private LoginHanderInterceptor loginHanderInterceptor;

    @Autowired
    public void setLoginHanderInterceptor(LoginHanderInterceptor loginHanderInterceptor) {
        this.loginHanderInterceptor = loginHanderInterceptor;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加对用户是否登录的拦截器，并添加过滤项、排除项
        registry.addInterceptor(loginHanderInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/assets/**", "/webjars/**")//排除样式、脚本、图片等资源文件
                .excludePathPatterns("/")//排除登录页面
                .excludePathPatterns("/foreLogin")//排除登录操作
                .excludePathPatterns("/backLogin")//排除登录操作
                .excludePathPatterns("/backLogin/check")//排除登录操作
                .excludePathPatterns("/foreLogin/check")//排除登录操作
                .excludePathPatterns("/register")//排除注册页面
                .excludePathPatterns("/registered");//排除注册页面
    }
}
