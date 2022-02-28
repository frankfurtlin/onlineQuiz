package com.frankfurtlin.onlineQuiz.domain;

import lombok.*;

@Data
public class Student {
    private int studentId;
    private String studentName;
    private String studentAccount;
    private String studentGender;
    private String studentEmail;
    private String studentPwd;
    private int classeId;
    private Classe classe;
}
