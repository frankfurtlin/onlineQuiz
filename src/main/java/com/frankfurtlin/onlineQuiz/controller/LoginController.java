package com.frankfurtlin.onlineQuiz.controller;

import com.frankfurtlin.onlineQuiz.domain.*;
import com.frankfurtlin.onlineQuiz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {

    private StudentService studentService;

    private TeacherService teacherService;

    private ClasseService classeService;

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Autowired
    public void setClasseService(ClasseService classeService) {
        this.classeService = classeService;
    }

    //学生登陆,首页
    @RequestMapping("/")
    public String view() {
        return "stage/login";
    }

    //前台学生登录验证
    @ResponseBody
    @RequestMapping("/foreLogin/check")
    public Object foreCheck(Student student, HttpServletRequest request) {
        AjaxResult result = new AjaxResult();
        HttpSession session = request.getSession();
        Student stud = studentService.check(student);
        if (stud != null) {
            session.setAttribute("loger", stud);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    //学生注册
    @RequestMapping("/register")
    public String toAddStudent(Model model) {
        List<Classe> allClasees = classeService.getAll();
        model.addAttribute("allClasees", allClasees);
        return "stage/register";
    }

    //注册成功具体操作
    @RequestMapping("/registered")
    public String AddStudent(Student student) {
        studentService.AddStudent(student);
        return "redirect:/";
    }

    //后台教师、管理员登录
    @RequestMapping("/backLogin")
    public String backLogin() {
        return "stage/loginx";
    }

    //后台教师、管理员登录验证
    @ResponseBody
    @RequestMapping("/backLogin/check")
    public Object backCheck(Teacher teacher, HttpServletRequest request) {
        AjaxResult result = new AjaxResult();
        HttpSession session = request.getSession();
        Teacher teac = teacherService.check(teacher);
        if (teac != null) {
            session.setAttribute("logerd", teac);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    //前台登录到展示页面
    @RequestMapping("/index")
    public String indexprexam() {
        return "stage/index";
    }

    //后台登陆到展示页面
    @RequestMapping("/main")
    public String index() {
        return "stage/main";
    }

    //退出系统
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        //session里可能不止存放一个数据，移除麻烦，所以让其失效更直接
        session.invalidate();
        return "redirect:/";
    }

}
