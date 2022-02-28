package com.frankfurtlin.onlineQuiz.domain;

import lombok.Data;

@Data
public class AjaxResult {
    private boolean success;

    public AjaxResult() {
    }

    public boolean isSuccess() {
        return success;
    }

}
