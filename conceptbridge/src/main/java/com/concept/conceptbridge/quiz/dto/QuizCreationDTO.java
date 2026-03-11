package com.concept.conceptbridge.quiz.dto;


import com.concept.conceptbridge.quiz.Quiz;
import com.concept.conceptbridge.quiz.QuizQuestion;

import java.util.List;

public class QuizCreationDTO {
    private Quiz quiz;
    private List<QuizQuestion> questions;

    // Constructors
    public QuizCreationDTO() {}

    public QuizCreationDTO(Quiz quiz, List<QuizQuestion> questions) {
        this.quiz = quiz;
        this.questions = questions;
    }

    // Getters and Setters
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
    }
}