package com.heycode.aizi.models;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    private String test;
    private int score;
    List<String> selectedAnswer;

    public Result(String test, int score, List<String> selectedAnswer) {
        this.test = test;
        this.score = score;
        this.selectedAnswer = selectedAnswer;
    }

    public Result() {

    }

    public List<String> getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(List<String> selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
