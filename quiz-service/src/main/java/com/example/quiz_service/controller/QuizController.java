package com.example.quiz_service.controller;

import com.example.quiz_service.model.Quiz;
import com.example.quiz_service.service.QuizService;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuiz();
    }

    @GetMapping("/{id}")
    public Quiz getQuizById(@PathVariable Long id) {
        return quizService.getQuizById(id.intValue());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Quiz updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody Quiz quiz) {
        try{
            return quizService.updateQuiz(id.intValue(), quiz);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id.intValue());
    }
}
