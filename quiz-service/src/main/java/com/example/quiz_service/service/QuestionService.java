package com.example.quiz_service.service;

import com.example.quiz_service.DTO.QuestionRequest;
import com.example.quiz_service.DTO.QuestionResponse;
import com.example.quiz_service.model.Question;
import com.example.quiz_service.model.QuestionOptions;
import com.example.quiz_service.repository.QuestionRepo;
import com.example.quiz_service.repository.QuizRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepo questionRepository;
    private final QuizRepo quizRepository;

    public QuestionService(
            QuestionRepo questionRepository,
            QuizRepo quizRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
    }

    public List<QuestionResponse> getQuestionsByQuizId(Integer quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new RuntimeException("Quiz not found");
        }

        return questionRepository.findByQuizId(Long.valueOf(quizId))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        return toResponse(question);
    }

    @Transactional
    public QuestionResponse createQuestion(QuestionRequest request) {
        if (!quizRepository.existsById(request.getQuizId().intValue())) {
            throw new RuntimeException("Quiz not found");
        }

        validateQuestion(request);

        Question question = new Question();
        question.setQuizId(request.getQuizId());
        question.setQuestionText(request.getQuestionText());
        question.setType(request.getType());
        question.setMarks(request.getMarks());

        for (QuestionRequest.OptionRequest optionRequest : request.getOptions()) {
            QuestionOptions option = new QuestionOptions();
            option.setOptionText(optionRequest.getOptionText());
            option.setCorrect(optionRequest.isCorrect());
            question.addOption(option);
        }

        return toResponse(questionRepository.save(question));
    }

    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!quizRepository.existsById(request.getQuizId().intValue())) {
            throw new RuntimeException("Quiz not found");
        }

        validateQuestion(request);

        question.setQuizId(request.getQuizId());
        question.setQuestionText(request.getQuestionText());
        question.setType(request.getType());
        question.setMarks(request.getMarks());

        question.getOptions().clear();

        for (QuestionRequest.OptionRequest optionRequest : request.getOptions()) {
            QuestionOptions option = new QuestionOptions();
            option.setOptionText(optionRequest.getOptionText());
            option.setCorrect(optionRequest.isCorrect());
            question.addOption(option);
        }

        return toResponse(questionRepository.save(question));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        questionRepository.delete(question);
    }

    private void validateQuestion(QuestionRequest request) {
        String type = request.getType();

        if (!"SINGLE_CHOICE".equals(type) &&
            !"MULTIPLE_CHOICE".equals(type)) {
            throw new IllegalArgumentException("Unsupported question type");
        }

        long correctCount = request.getOptions().stream()
                .filter(QuestionRequest.OptionRequest::isCorrect)
                .count();

        if ("SINGLE_CHOICE".equals(type) && correctCount != 1) {
            throw new IllegalArgumentException(
                    "Single-choice questions require exactly one correct option");
        }

        if ("MULTIPLE_CHOICE".equals(type) && correctCount < 1) {
            throw new IllegalArgumentException(
                    "Multiple-choice questions require at least one correct option");
        }
    }

    private QuestionResponse toResponse(Question question) {
        List<String> optionTexts = question.getOptions()
                .stream()
                .map(QuestionOptions::getOptionText)
                .toList();

        return new QuestionResponse(
                question.getId(),
                question.getQuizId(),
                question.getQuestionText(),
                question.getType(),
                question.getMarks(),
                optionTexts
        );
    }
}
