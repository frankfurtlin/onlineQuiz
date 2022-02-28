package com.frankfurtlin.onlineQuiz.service.impl;

import com.frankfurtlin.onlineQuiz.domain.Exam;
import com.frankfurtlin.onlineQuiz.mapper.ExamMapper;
import com.frankfurtlin.onlineQuiz.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private ExamMapper examMapper;

    @Autowired
    public void setExamMapper(ExamMapper examMapper) {
        this.examMapper = examMapper;
    }

    @Override
    public List<Exam> getAll() {
        return examMapper.queryAll();
    }

    /*@Override
    public int queryCOuntALlExam() {
        return 0;
    }*/

    @Override
    public void AddExam(Exam exam) {
        examMapper.AddExam(exam);
    }

    @Override
    public Exam getExamById(Integer id) {
        return examMapper.getExamById(id);
    }

    /*@Override
    public void EditExam(Exam exam) {
        examMapper.EditExam(exam);
    }*/

    @Override
    public void deleteById(Integer id) {
        examMapper.deleteById(id);
    }

    @Override
    public List<Exam> getAllS() {
        return examMapper.getAllS();
    }

}
