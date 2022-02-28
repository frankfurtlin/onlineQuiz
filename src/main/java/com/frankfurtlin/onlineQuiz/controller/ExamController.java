package com.frankfurtlin.onlineQuiz.controller;

import com.frankfurtlin.onlineQuiz.domain.*;
import com.frankfurtlin.onlineQuiz.service.ExamService;
import com.frankfurtlin.onlineQuiz.service.PaperService;
import com.frankfurtlin.onlineQuiz.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/exam")
public class ExamController {

    private ExamService examService;
    private PaperService paperService;
    private RecordService recordService;

    @Autowired
    public void setExamService(ExamService examService) {
        this.examService = examService;
    }

    @Autowired
    public void setPaperService(PaperService paperService) {
        this.paperService = paperService;
    }

    @Autowired
    public void setRecordService(RecordService recordService) {
        this.recordService = recordService;
    }

    //前台学生考试安排查询
    @RequestMapping("/toExam")
    public String toExam(Model model) {
        List<Exam> Exams = examService.getAll();
        model.addAttribute("Exams", Exams);
        return "exam/examplan";
    }

    //前台学生历史考试查询
    @RequestMapping("/toHist/{id}")
    public String toHist(@PathVariable("id") Integer id, Model model) {
        List<Record> records = recordService.queryAllExamById(id);
        model.addAttribute("records", records);
        return "exam/histplan";
    }

    //前台学生进行考试
    @RequestMapping("/toDoExam/{id}")
    public String toDoExam(@PathVariable("id") Integer id, Model model, String examId) {
        List<QuestionPaper> questionPapers = paperService.paperQueryALlQuestionByIdOrderByType(id);
        int exId = Integer.parseInt(examId);
        Exam exam = examService.getExamById(exId);
        Paper paperName = paperService.queryPaperNameById(exam.getPaperId());
        model.addAttribute("paperName", paperName);
        model.addAttribute("examById", exam);
        model.addAttribute("questionPapers", questionPapers);
        return "exam/doExam";
    }

    //提交试卷
    @RequestMapping("/submitExam")
    public String submitExam(Integer paperId, Integer studentId, HttpServletRequest request) {
        List<QuestionPaper> questionPapers = paperService.paperQueryALlQuestionByIdOrderByType(paperId);
        List<String> ans = new ArrayList<>();
        List<String> RightAns = new ArrayList<>();
        for (QuestionPaper qb : questionPapers) {
            RightAns.add(qb.getQuestion().getQuestionOpright());
            StringBuilder parameter = new StringBuilder();
            String[] parameters;
            if (qb.getQuestion().getQuestionType().equals("y")) {
                parameters = request.getParameterValues("optionsSelect" + qb.getQuestionId());
                for (String s : parameters) {
                    parameter.append(s);
                }
            } else {
                parameter.append(request.getParameter("optionsSelect" + qb.getQuestionId()));
            }
            ans.add(parameter.toString());
        }
        //核对答案得到成绩
        int k = 0;    //哨兵
        double y = 0.0;    //正确数
        int score = 0;    //得分
        int a = 0;        //记录单选题个数
        int b = 0;        //记录多选题个数
        int c = 0;        //记录判断题个数
        int totalScore;
        for (QuestionPaper qb : questionPapers) {
            //若为单选题则正确+单选题分数
            if (qb.getQuestion().getQuestionType().equals("x")) {
                if (ans.get(k).equals(RightAns.get(k))) {
                    score += qb.getPaper().getScoreSin();
                    y++;
                }
                a++;
                k++;
            } else if (qb.getQuestion().getQuestionType().equals("y")) {
                if (ans.get(k).equals(RightAns.get(k))) {
                    score += qb.getPaper().getScoreChe();
                    y++;
                }
                b++;
                k++;
            } else {
                if (ans.get(k).equals(RightAns.get(k))) {
                    score += qb.getPaper().getScoreJug();
                    y++;
                }
                c++;
                k++;
            }
        }
        int scoreSin1 = questionPapers.get(0).getPaper().getScoreSin();
        int scoreChe1 = questionPapers.get(0).getPaper().getScoreChe();
        int scoreJug1 = questionPapers.get(0).getPaper().getScoreJug();
        int bool = recordService.queryBooleanToscore(paperId);
        if (bool == 0) {
            totalScore = scoreSin1 * a + scoreChe1 * b + scoreJug1 * c; //得到每张试卷总分
            Toscore toscore = new Toscore();
            toscore.setPaperId(paperId);
            toscore.setToscore(totalScore);
            recordService.AddToScore(toscore);
        }
        //保存答题记录
        String answer = String.join(",", ans);
        Paper paper = paperService.queryPaperNameById(paperId);
        String paperName = paper.getPaperName();
        Double recordAcc = y / k;
        int recordScore = score;
        Record record = new Record();
        record.setRecordName(paperName);
        record.setStudentId(studentId);
        record.setPaperId(paperId);
        record.setRecordAnswer(answer);
        record.setRecordAcc(recordAcc);
        record.setRecordScore(recordScore);
        recordService.addRecord(record);
        return "redirect:/index";
    }

    //后台查看所有考试安排
    @RequestMapping("/getAllExam")
    public String getAllExam(Model model) {
        List<Exam> Exams = examService.getAllS();
        model.addAttribute("Exams", Exams);
        return "exam/backexamlist";
    }

    //后台添加考试安排
    @RequestMapping("/toAddExam")
    public String toAddExam(Model model) {
        List<Paper> papers = paperService.getAll();
        model.addAttribute("papers", papers);
        return "exam/AddExam";
    }

    //添加考试具体操作
    @RequestMapping("/addExam")
    public String addExam(Exam exam, String examBegins, String examEnds) throws ParseException {
        String t1 = examBegins.replace("T", " ");
        String t2 = examEnds.replace("T", " ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date begin = sdf.parse(t1);
        Date end = sdf.parse(t2);
        exam.setExamBegin(begin);
        exam.setExamEnd(end);
        examService.AddExam(exam);
        return "redirect:/exam/getAllExam";
    }

    //后台删除考试安排
    @RequestMapping("/deleteExam/{id}")
    public String toEditExam(@PathVariable("id") Integer id) {
        examService.deleteById(id);
        return "redirect:/exam/getAllExam";
    }
}
