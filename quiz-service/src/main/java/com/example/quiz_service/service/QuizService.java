package com.example.quiz_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.quiz_service.model.Quiz;
import com.example.quiz_service.repository.QuizRepo;

@Service 
public class QuizService {
    @Autowired 
    private QuizRepo quizRepo;

    @Cacheable (value = "quizzes",key = "'active'")
    public List<Quiz> getAllQuiz(){
        return quizRepo.findByActiveTrue();
    }

    @Cacheable (value = "quiz",key = "#id")
    public Quiz getQuizById(Integer quizId){
        return quizRepo.findById(quizId)
                .orElseThrow(()->new RuntimeException("Quiz Not Found"));
    }

    @CacheEvict (value = {"quizzes","quiz"},allEntries = true)
    public Quiz createQuiz(Quiz quiz){
        return quizRepo.save(quiz);
    }

    @CacheEvict (value = {"quizzes","quiz"},allEntries = true)
    public Quiz updateQuiz(Integer id,Quiz quiz) throws Exception{
        Quiz existingQuiz=getQuizById(id);

        existingQuiz.setTitle(quiz.getTitle());
        existingQuiz.setDescription(quiz.getDescription());
        existingQuiz.setActive(quiz.isAction());

        return quizRepo.save(existingQuiz);
    }

    @CacheEvict (value = {"quizzes","quiz"},allEntries = true)
    public void deleteQuiz(Integer id){
        Quiz quiz=getQuizById(id);

        quizRepo.delete(quiz);
    }

}
