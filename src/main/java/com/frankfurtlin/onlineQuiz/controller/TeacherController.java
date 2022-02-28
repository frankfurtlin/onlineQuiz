package com.frankfurtlin.onlineQuiz.controller;

import com.frankfurtlin.onlineQuiz.domain.Classe;
import com.frankfurtlin.onlineQuiz.domain.Teacher;
import com.frankfurtlin.onlineQuiz.service.ClasseService;
import com.frankfurtlin.onlineQuiz.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private TeacherService teacherService;

    private ClasseService classeService;

    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Autowired
    public void setClasseService(ClasseService classeService) {
        this.classeService = classeService;
    }

    //查看所有教师
    @RequestMapping("/getAllTeacher")
    public String getAllTeacher(Model model) {
        List<Teacher> teachers = teacherService.getAll();
        //查找classe表中已存在的教师，将用于表单教师是否可以删除
        List<Classe> classes = classeService.queryAllTeacherId();
        List<Integer> teaId = new ArrayList<>();
        for (Classe cla : classes) {
            teaId.add(cla.getTeacherId());
        }
        model.addAttribute("teaId", teaId);
        model.addAttribute("teachers", teachers);
        return "teacher/teacherList";
    }

    //教师添加操作
    @RequestMapping("/toAddTeacher")
    public String toAddTeacher() {
        return "teacher/teacherAdd";
    }

    //教师修改操作
    @RequestMapping("/{id}")
    public String toEditTeacher(@PathVariable("id") Integer id, Model model) {
        Teacher teacher = teacherService.getTeacherById(id);
        model.addAttribute("teacher", teacher);
        return "teacher/teacherAdd";
    }

    //添加或修改具体操作
    @RequestMapping("/addTeacher")
    public String addTeacher(Teacher teacher) {
        int teacherId = teacher.getTeacherId();
        if (teacherId == 0) {
            /*若id为0即是刚添加未分配，要进行增加操作*/
            teacherService.addTeacher(teacher);
        } else {
            /*若id已存在，是要进行修改操作*/
            teacherService.editTeacher(teacher);
        }
        return "redirect:/teacher/getAllTeacher";
    }

    //教师删除
    @RequestMapping("/deleteTeacher/{id}")
    public String deleteTeacherById(@PathVariable("id") Integer id) {
        teacherService.deleteTeacherById(id);
        return "redirect:/teacher/getAllTeacher";
    }

}
