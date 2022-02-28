package com.frankfurtlin.onlineQuiz.controller;

import com.frankfurtlin.onlineQuiz.component.ArrayRandom;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.frankfurtlin.onlineQuiz.domain.*;
import com.frankfurtlin.onlineQuiz.service.PaperService;
import com.frankfurtlin.onlineQuiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/paper")
public class PaperController {

    private PaperService paperService;

    private QuestionService questionService;

    @Autowired
    public void setPaperService(PaperService paperService) {
        this.paperService = paperService;
    }

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    //查看所有试卷
    @RequestMapping("/getAllPaper")
    public String getAllPaper(Model model) {
        List<Paper> papers = paperService.getAll();
        model.addAttribute("papers", papers);
        return "paper/paperList";
    }

    //试卷添加
    @RequestMapping("/toAddPaper")
    public String toAddPaper() {
        return "paper/paperAdd";
    }

    //试卷添加具体操作
    @RequestMapping("/addPaper")
    public String addPaper(Paper paper) {
        paperService.addPaper(paper);
        return "redirect:/paper/getAllPaper";
    }

    //试卷修改
    @RequestMapping("/toEditPaper/{id}")
    public String toEditPaper(@PathVariable("id") Integer id, Model model) {
        Paper paper = paperService.getPaperById(id);
        model.addAttribute("paper", paper);
        return "paper/paperEdit";
    }

    //试卷修改具体操作
    @RequestMapping("/EditPaper")
    public String toEditPaper(Paper paper) {
        paperService.editPaper(paper);
        return "redirect:/paper/getAllPaper";
    }

    //试卷删除
    @RequestMapping("/deletePaper/{id}")
    public String deletePaperById(@PathVariable("id") Integer id) {
        paperService.deletePaperById(id);
        return "redirect:/paper/getAllPaper";
    }

    //试卷的试题管理
    @RequestMapping("/toManagerQuestion/{id}")
    public String toManagerQuestion(@PathVariable("id") Integer id, Model model) {
        List<QuestionPaper> questionPapers = paperService.paperQueryALlQuestionById(id);
        model.addAttribute("papid", id);
        Paper paperName = paperService.queryPaperNameById(id);
        model.addAttribute("paperName", paperName.getPaperName());
        model.addAttribute("questionPapers", questionPapers);
        return "paper/ManagerQuestion";
    }

    //来到试题显示页面为试卷手动添加试题
    @RequestMapping("/toAddQuestion/{id}")
    public String getAllQuestion(@PathVariable("id") Integer paperId, Question question, @RequestParam(defaultValue = "1") int pageNum,
                                 @RequestParam(defaultValue = "4") int pageSize,
                                 Model model) {
        //查找所有题目课程和所有类型，且去重
        List<Question> questionCourses = questionService.queryAllCourse();
        questionCourses.add(new Question("bug", "all"));
        List<Question> questionTypes = questionService.queryAllType();
        questionTypes.add(new Question("k", "bug"));
        String questionCourseBef = question.getQuestionCourse();
        String questionCourseresRes;
        if (questionCourseBef == null) {
            //默认查询所有
            questionCourseresRes = "all";
        } else {
            questionCourseresRes = questionCourseBef;
        }
        //若是第一次查询则用上次提交的表单中的类型、课程，若是第二次查询则延用上次类型
        String questionTypeBef = question.getQuestionType();
        String questionTypesRes;
        if (questionTypeBef == null) {
            //默认查询所有
            questionTypesRes = "k";
        } else {
            questionTypesRes = questionTypeBef;
        }
        //查询试卷中已存在的试题，不能再次被添加
        List<Question> questionids = paperService.queryALlQuestionIdInPaperById(paperId);
        List<Integer> quesds = new ArrayList<>();
        if (questionids != null) {
            for (Question qid : questionids) {
                quesds.add(qid.getQuestionId());
            }
        }
        model.addAttribute("quesds", quesds);
        //分页
        PageHelper.startPage(pageNum, pageSize);//这行是重点，表示从pageNum页开始，每页pageSize条数据
        List<Question> questions = questionService.getAll(question);
        PageInfo<Question> pageInfo = new PageInfo<>(questions);
        Paper paperName = paperService.queryPaperNameById(paperId);
        model.addAttribute("paperName", paperName.getPaperName());
        model.addAttribute("questionCourseresRes", questionCourseresRes);
        model.addAttribute("questionTypesRes", questionTypesRes);
        model.addAttribute("questionTypes", questionTypes);
        model.addAttribute("questionCourses", questionCourses);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("paperId", paperId);
        return "paper/AddQuestion";
    }

    //为试卷手动添加试题
    @RequestMapping("/AddQuestion")
    public String AddQuestion(Integer paperId, Integer quesId, Integer pageNum, String questionCourse, String questionType) {
        QuestionPaper questionPaper = new QuestionPaper(quesId, paperId);
        paperService.AddQuestionToPaperById(questionPaper);
        return "redirect:/paper/toAddQuestion/" + paperId + "?pageNum=" + pageNum + "&questionCourse=" + questionCourse + "&questionType=" + questionType;
    }

    //从试卷中移除试题
    @RequestMapping("/detachQuestion")
    public String detachQuestion(Integer qpId, Integer paperId) {
        paperService.detachQuestionById(qpId);
        return "redirect:/paper/toManagerQuestion/" + paperId;
    }

    //去往随机组题页面
    @RequestMapping("/toRandomQuestion/{id}")
    public String toRandomQuestion(@PathVariable("id") Integer papid, Model model) {
        List<Question> questionCourses = questionService.queryAllCourse();
        questionCourses.add(new Question("bug", "all"));
        //查找所有题目课程和所有类型，且去重
        //用于条件查询题库中尚未分配的题有多少道
        int TotalQuestionNums = questionService.queryAllQuestionNums();
        List<Map> maps = questionService.queryNumOfQuestionType();
        List<String> course = new ArrayList<>();
        List<Integer> count = new ArrayList<>();
        for (Map map : maps) {
            for (Object key : map.keySet()) {
                if (map.get(key) instanceof String) {
                    course.add(map.get(key).toString());
                } else {
                    count.add(Integer.parseInt(map.get(key).toString()));
                }
            }
        }
        Paper paperName = paperService.queryPaperNameById(papid);
        model.addAttribute("paperName", paperName.getPaperName());
        model.addAttribute("count", count);
        model.addAttribute("course", course);
        model.addAttribute("TotalQuestionNums", TotalQuestionNums);
        model.addAttribute("paperId", papid);
        model.addAttribute("questionCourses", questionCourses);
        return "paper/RandomQuestion";
    }

    //指定试卷id和课程随机生成试题
    @RequestMapping("/RandomADDQuestion/{id}")
    public String RandomADDQuestion(@PathVariable("id") Integer papid, String questionCourse, int QuesNums) {

        //找到试卷所有未分配的试题
        PapIdQuesCourse papIdQuesCourse = new PapIdQuesCourse();
        papIdQuesCourse.setPapid(papid);
        papIdQuesCourse.setQuestionCourse(questionCourse);
        List<Question> questionsNodivIds = questionService.queryAllQueIdNotInPaperById(papIdQuesCourse);

        //将试题编号保存到list
        List<Integer> list = new ArrayList<>();
        for (Question question : questionsNodivIds) {
            list.add(question.getQuestionId());
        }

        //随机打乱
        /*Collections.shuffle(list);
        for(int i=0;i<QuesNums;i++){
            QuestionPaper questionPaper=new QuestionPaper(list.get(i),papid);
            paperService.AddQuestionToPaperById(questionPaper);
        }*/

        //随机产生QuesNums个不同的数组下标
        int[] array = ArrayRandom.random(QuesNums, list.size());
        for (int i = 0; i < QuesNums; i++) {
            QuestionPaper questionPaper = new QuestionPaper(list.get(array[i]), papid);
            paperService.AddQuestionToPaperById(questionPaper);
        }

        return "redirect:/paper/toManagerQuestion/" + papid;
    }

    //删除所有的试题
    @RequestMapping("/deleteAllQues/{id}")
    public String deleteAllQues(@PathVariable("id") Integer papid) {
        //int nums=questionService.queryIdByPapQue(ques);
        return "redirect:/paper/toManagerQuestion/" + papid;
    }
}
