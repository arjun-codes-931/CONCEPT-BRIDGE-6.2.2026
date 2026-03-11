package com.concept.conceptbridge.quiz;

import com.concept.conceptbridge.quiz.dto.AnswerDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository questionRepository;

    @Autowired
    private StudentQuizAttemptRepository attemptRepository;

    @Autowired
    private StudentQuizAnswerRepository answerRepository;

    // ===============================
    // GET AVAILABLE QUIZZES
    // ===============================
    public List<Quiz> getAvailableQuizzes(String branch, Integer semester, String section) {
        return quizRepository.findVisibleQuizzes(branch, semester, section);
    }
    public Quiz getQuizById(Long quizId) {
        return quizRepository.findById(quizId).orElseThrow();
    }
    public List<AnswerDTO> getSavedAnswers(Long attemptId) {

        List<StudentQuizAnswer> answers =
                answerRepository.findByAttemptId(attemptId);

        return answers.stream().map(a -> {

            AnswerDTO dto = new AnswerDTO();
            dto.setQuestionId(a.getQuestion().getId());
            dto.setSelectedOption(a.getSelectedOption());

            return dto;

        }).toList();
    }
    public void saveAnswer(Long attemptId, AnswerDTO dto) {

        StudentQuizAttempt attempt =
                attemptRepository.findById(attemptId).orElseThrow();

        QuizQuestion question =
                questionRepository.findById(dto.getQuestionId()).orElseThrow();

        Optional<StudentQuizAnswer> existing =
                answerRepository.findByAttemptIdAndQuestionId(attemptId, dto.getQuestionId());

        StudentQuizAnswer answer;

        if(existing.isPresent()) {

            answer = existing.get();
            answer.setSelectedOption(dto.getSelectedOption());

        } else {

            answer = new StudentQuizAnswer();
            answer.setAttempt(attempt);
            answer.setQuestion(question);
            answer.setSelectedOption(dto.getSelectedOption());

        }

        answerRepository.save(answer);
    }
    public List<QuizQuestion> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }
    // ===============================
    // START QUIZ
    // ===============================
    @Transactional
    public StudentQuizAttempt startQuiz(Long studentId, Long quizId) {

        // Just find quiz
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found: " + quizId));

        // ALWAYS create new attempt - don't check for existing
        StudentQuizAttempt attempt = new StudentQuizAttempt();
        attempt.setStudentId(studentId);
        attempt.setQuiz(quiz);
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setStatus(AttemptStatus.IN_PROGRESS);
        attempt.setScore(0);

        return attemptRepository.save(attempt);
    }

    // ===============================
    // SUBMIT QUIZ
    // ===============================
    @Transactional
    public StudentQuizAttempt submitQuiz(Long attemptId, List<AnswerDTO> answerDTOs) {
        StudentQuizAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found: " + attemptId));

        // Prevent re-submission
        if (attempt.getStatus() == AttemptStatus.SUBMITTED) {
            throw new RuntimeException("Quiz already submitted");
        }

        int score = 0;
        List<StudentQuizAnswer> answers = new ArrayList<>();

        for (AnswerDTO dto : answerDTOs) {
            QuizQuestion question = questionRepository.findById(dto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found: " + dto.getQuestionId()));

            boolean isCorrect = question.getCorrectOption().equals(dto.getSelectedOption());

            StudentQuizAnswer answer = new StudentQuizAnswer();
            answer.setAttempt(attempt);
            answer.setQuestion(question);
            answer.setSelectedOption(dto.getSelectedOption());
            answer.setCorrect(isCorrect);

            answers.add(answer);

            if (isCorrect) {
                score += question.getMarks();
            }
        }

        // Save all answers
        answerRepository.saveAll(answers);

        // Update attempt
        attempt.setScore(score);
        attempt.setStatus(AttemptStatus.SUBMITTED);
        attempt.setSubmittedAt(LocalDateTime.now());

        return attemptRepository.save(attempt);
    }

    // ===============================
    // GET QUIZ RESULT
    // ===============================
    public StudentQuizAttempt getQuizResult(Long attemptId) {
        return attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Result not found"));
    }
}