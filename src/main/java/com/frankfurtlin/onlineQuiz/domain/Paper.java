package com.frankfurtlin.onlineQuiz.domain;

import lombok.Data;

@Data
public class Paper {
    private int paperId;
    private String paperName;
    private int scoreSin;
    private int scoreChe;
    private int scoreJug;
}
