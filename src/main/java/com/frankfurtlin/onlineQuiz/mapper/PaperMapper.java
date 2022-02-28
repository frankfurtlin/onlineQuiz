package com.frankfurtlin.onlineQuiz.mapper;

import com.frankfurtlin.onlineQuiz.domain.Paper;
import com.frankfurtlin.onlineQuiz.domain.Question;
import com.frankfurtlin.onlineQuiz.domain.QuestionPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PaperMapper {
    List<Paper> queryAll();

    void addPaper(Paper paper);

    Paper queryPaperById(Integer id);

    void editPaper(Paper paper);

    void deletePaperById(Integer id);

    List<Paper> queryPaperNotAdvisor();

    List<Question> queryALlQuestionId();

    List<QuestionPaper> paperQueryALlQuestionById(Integer id);

    List<Question> queryALlQuestionIdInPaperById(Integer paperId);

    Paper queryPaperNameById(Integer id);

    void AddQuestionToPaperById(QuestionPaper questionPaper);

    void detachQuestionById(Integer qpId);

    @Select("select count(*) from paper")
    int queryCountALlPaps();

    List<QuestionPaper> paperQueryALlQuestionByIdOrderByType(Integer id);

//    List<QuestionPaper> paperQueryALlQuestionIDByPaperIdOrderByType(Integer paperId);
}
