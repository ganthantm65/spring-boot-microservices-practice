package com.example.quiz_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_service.model.Question;

public interface QuestionRepo extends JpaRepository<Question,Long> {
     List<Question> findByQuizId(Long quizId);
}
