package com.frankfurtlin.onlineQuiz.controller;

import com.frankfurtlin.onlineQuiz.domain.*;
import com.frankfurtlin.onlineQuiz.service.ClasseService;
import com.frankfurtlin.onlineQuiz.service.PaperService;
import com.frankfurtlin.onlineQuiz.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/record")
public class RecordController {

    RecordService recordService;

    PaperService paperService;

    ClasseService classeService;

    @Autowired
    public void setRecordService(RecordService recordService) {
        this.recordService = recordService;
    }

    @Autowired
    public void setPaperService(PaperService paperService) {
        this.paperService = paperService;
    }

    @Autowired
    public void setClasseService(ClasseService classeService) {
        this.classeService = classeService;
    }

    //获取所有记录
    @RequestMapping("/getAllRecord")
    public String getAllRecord(Model model) {
        List<Record> records = recordService.queryAll();
        model.addAttribute("records", records);
        return "record/RecordList";
    }

    //删除记录
    @RequestMapping("/deleteRecore/{id}")
    public String deleteRecore(@PathVariable("id") Integer id) {
        recordService.deleteById(id);
        return "redirect:/record/getAllRecord";
    }

    //根据记录id获取试卷详情
    @RequestMapping("/toShowExamHist/{id}")
    public String toShowExamHist(@PathVariable("id") Integer id, Model model) {
        //通过记录id查找试卷和答题记录
        Integer papid = recordService.queryByRecordId(id);
        String answers = recordService.queryAnsByRecordId(id);
        //原始试卷
        List<QuestionPaper> questionPapers = paperService.paperQueryALlQuestionByIdOrderByType(papid);
        //提交过的答案
        List<String> ans = Arrays.asList(answers.split(","));
        model.addAttribute("questionPapers", questionPapers);
        model.addAttribute("ans", ans);
        return "record/showExamHist";
    }

}
