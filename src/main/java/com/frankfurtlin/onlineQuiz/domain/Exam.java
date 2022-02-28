package com.frankfurtlin.onlineQuiz.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Exam {
    private int examId;
    private int paperId;
    private Date examBegin;
    private Date examEnd;
    private Paper paper;
}
