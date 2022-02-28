package com.frankfurtlin.onlineQuiz.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.frankfurtlin.onlineQuiz.domain.Question;
import com.frankfurtlin.onlineQuiz.service.PaperService;
import com.frankfurtlin.onlineQuiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/question")
public class QuestionController {

    private QuestionService questionService;

    private PaperService paperService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Autowired
    public void setPaperService(PaperService paperService) {
        this.paperService = paperService;
    }


    //查看所有试题 pagesize控制每页数据条数
    @RequestMapping("/getAllQuestion")
    public String getAllQuestion(Question question, @RequestParam(defaultValue = "1") int pageNum,
                                 @RequestParam(defaultValue = "4") int pageSize,
                                 Model model) {
        //查找所有题目课程和所有类型，且去重
        List<Question> questionCourses = questionService.queryAllCourse();
        questionCourses.add(new Question("bug", "all"));
        List<Question> questionTypes = questionService.queryAllType();
        questionTypes.add(new Question("k", "bug"));

        //查询试题课程
        String questionCourseBef = question.getQuestionCourse();
        String questionCourseresRes;
        if (questionCourseBef == null) {
            //默认查询所有
            questionCourseresRes = "all";
        } else {
            questionCourseresRes = questionCourseBef;
        }

        //查询试题类型
        String questionTypeBef = question.getQuestionType();
        String questionTypesRes;
        if (questionTypeBef == null) {
            //默认查询所有
            questionTypesRes = "k";
        } else {
            questionTypesRes = questionTypeBef;
        }

        //获取所有试题ID
        List<Question> questionids = paperService.queryALlQuestionId();
        List<Integer> quesIds = new ArrayList<>();
        for (Question qid : questionids) {
            quesIds.add(qid.getQuestionId());
        }
        model.addAttribute("quesIds", quesIds);

        PageHelper.startPage(pageNum, pageSize);//这行是重点，表示从pageNum页开始，每页pageSize条数据
        List<Question> questions = questionService.getAll(question);
        PageInfo<Question> pageInfo = new PageInfo<>(questions);

        model.addAttribute("questionCourseresRes", questionCourseresRes);
        model.addAttribute("questionTypesRes", questionTypesRes);
        model.addAttribute("questionTypes", questionTypes);
        model.addAttribute("questionCourses", questionCourses);
        model.addAttribute("pageInfo", pageInfo);
        return "question/questionList";
    }

    //试题添加操作
    @RequestMapping("/toAddQuestion")
    public String toAddQuestion(Model model) {
        List<Question> questionCourses = questionService.queryAllCourse();
        List<Question> questionTypes = questionService.queryAllType();
        model.addAttribute("questionTypes", questionTypes);
        model.addAttribute("questionCourses", questionCourses);
        return "question/questionAdd";
    }

    //试题添加具体操作
    @RequestMapping("/addQuestion")
    public String addQuestion(Question question) {
        questionService.addQuestion(question);
        return "redirect:/question/getAllQuestion";
    }

    //试题修改操作
    @RequestMapping("/toEditQuestion/{id}")
    public String toEditQuestion(@PathVariable("id") Integer id, Model model) {
        List<Question> questionCourses = questionService.queryAllCourse();
        List<Question> questionTypes = questionService.queryAllType();
        Question question = questionService.getQuestionById(id);
        model.addAttribute("questionTypes", questionTypes);
        model.addAttribute("questionCourses", questionCourses);
        model.addAttribute("question", question);
        return "question/questionEdit";
    }

    //试题修改具体操作
    @RequestMapping("/EditQuestion")
    public String EditQuestion(Question question) {
        questionService.editQuestion(question);
        return "redirect:/question/getAllQuestion";
    }

    //试题删除
    @RequestMapping("/deleteQuestion/{id}")
    public String deleteQuestionById(@PathVariable("id") Integer id) {
        questionService.deleteQuestionById(id);
        return "redirect:/question/getAllQuestion";
    }

}
