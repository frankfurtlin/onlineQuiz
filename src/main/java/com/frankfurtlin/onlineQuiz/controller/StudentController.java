package com.frankfurtlin.onlineQuiz.controller;

import com.frankfurtlin.onlineQuiz.domain.Classe;
import com.frankfurtlin.onlineQuiz.domain.Student;
import com.frankfurtlin.onlineQuiz.mapper.ClasseMapper;
import com.frankfurtlin.onlineQuiz.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private ClasseMapper classeMapper;

    private StudentService studentService;

    @Autowired
    public void setClasseMapper(ClasseMapper classeMapper) {
        this.classeMapper = classeMapper;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    //查看所有学生
    @RequestMapping("/getAllStudent")
    public String getAllStudent(Model model) {
        List<Student> students = studentService.getAll();
        model.addAttribute("students", students);
        return "student/studentList";
    }

    //修改学生操作
    @RequestMapping("/{id}")
    public String updateStudent(@PathVariable("id") Integer id, Model model) {
        Student student = studentService.getStudentById(id);
        List<Classe> classes = classeMapper.queryAll();
        model.addAttribute("classes", classes);
        model.addAttribute("student", student);
        return "student/studentEdit";
    }

    //更改学生具体操作
    @RequestMapping("/editStudent")
    public String EditStudent(Student student) {
        studentService.EditStudent(student);
        return "redirect:/student/getAllStudent";
    }

    //删除学生
    @RequestMapping("/deleteStudent/{id}")
    public String deleteStudent(@PathVariable("id") Integer id) {
        studentService.deleteById(id);
        return "redirect:/student/getAllStudent";
    }
}
