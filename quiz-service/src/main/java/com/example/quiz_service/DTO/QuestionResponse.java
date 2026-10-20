package com.example.quiz_service.DTO;

import java.util.List;

public class QuestionResponse {

    private Long id;
    private Long quizId;
    private String questionText;
    private String type;
    private int marks;
    private List<String> options;

    public QuestionResponse(
            Long id,
            Long quizId,
            String questionText,
            String type,
            int marks,
            List<String> options) {
        this.id = id;
        this.quizId = quizId;
        this.questionText = questionText;
        this.type = type;
        this.marks = marks;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getType() {
        return type;
    }

    public int getMarks() {
        return marks;
    }

    public List<String> getOptions() {
        return options;
    }
}