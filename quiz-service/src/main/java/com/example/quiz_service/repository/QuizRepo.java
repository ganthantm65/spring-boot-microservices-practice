package com.example.quiz_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_service.model.Quiz;

public interface QuizRepo extends JpaRepository<Quiz,Integer>{
    List<Quiz> findByActiveTrue();
}
