package com.example.quiz_service.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class QuestionRequest {

    @NotNull
    private Long quizId;

    @NotBlank
    private String questionText;

    @NotBlank
    private String type = "SINGLE_CHOICE";

    @Min(1)
    private int marks = 1;

    @Valid
    @NotEmpty
    private List<OptionRequest> options;

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public List<OptionRequest> getOptions() {
        return options;
    }

    public void setOptions(List<OptionRequest> options) {
        this.options = options;
    }

    public static class OptionRequest {

        @NotBlank
        private String optionText;

        private boolean correct;

        public String getOptionText() {
            return optionText;
        }

        public void setOptionText(String optionText) {
            this.optionText = optionText;
        }

        public boolean isCorrect() {
            return correct;
        }

        public void setCorrect(boolean correct) {
            this.correct = correct;
        }
    }
}