package com.frankfurtlin.onlineQuiz.component;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
@Configuration
public class LoginHanderInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //学生登录
        Object user = request.getSession().getAttribute("loger");
        //后台教师、管理员登录
        Object userd = request.getSession().getAttribute("logerd");
        if (user != null || userd != null) {
            return true;
        } else {
            request.setAttribute("msg", "没有权限先登录");
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
