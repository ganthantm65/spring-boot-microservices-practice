package com.example.quiz_service.controller;

import com.example.quiz_service.DTO.QuestionRequest;
import com.example.quiz_service.DTO.QuestionResponse;
import com.example.quiz_service.service.QuestionService;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/quiz/{quizId}")
    public List<QuestionResponse> getQuestionsByQuizId(
            @PathVariable Long quizId) {
        return questionService.getQuestionsByQuizId(quizId.intValue());
    }

    @GetMapping("/{id}")
    public QuestionResponse getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public QuestionResponse createQuestion(
            @Valid @RequestBody QuestionRequest request) {
        return questionService.createQuestion(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public QuestionResponse updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequest request) {
        return questionService.updateQuestion(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }
}
