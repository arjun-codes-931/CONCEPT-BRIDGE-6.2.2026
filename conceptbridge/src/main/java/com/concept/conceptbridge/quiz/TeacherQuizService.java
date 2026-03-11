package com.concept.conceptbridge.quiz;


import com.concept.conceptbridge.quiz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TeacherQuizService {

    @Autowired
    private TeacherQuizRepository teacherQuizRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository questionRepository;



    @Autowired
    private StudentQuizAttemptRepository attemptRepository;

    @Autowired
    private StudentQuizAnswerRepository answerRepository;

    // ==================== QUIZ LISTING ====================

    /**
     * Get all quizzes created by a specific teacher
     */
    public List<Quiz> getQuizzesByTeacher(Long teacherId) {
        return teacherQuizRepository.findByCreatedByOrderByCreatedAtDesc(teacherId);
    }

    /**
     * Get quizzes with optional filters
     */
    public List<Quiz> getQuizzesByTeacherWithFilters(Long teacherId, String branch, String subject) {
        return teacherQuizRepository.findByTeacherWithFilters(teacherId, branch, subject);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    // ==================== QUIZ DETAILS ====================

    /**
     * Get quiz details with questions
     */
    public Map<String, Object> getQuizDetails(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        List<QuizQuestion> questions = questionRepository.findByQuizIdOrderByIdAsc(quizId);

        Map<String, Object> result = new HashMap<>();
        result.put("quiz", quiz);
        result.put("questions", questions);
        result.put("totalQuestions", questions.size());

        return result;
    }

    // ==================== QUIZ ATTEMPTS ====================

    /**
     * Get all attempts for a specific quiz
     */
    public List<StudentQuizAttempt> getQuizAttempts(Long quizId) {
        return attemptRepository.findByQuizId(quizId);
    }

    /**
     * Get attempts filtered by status
     */
    public List<StudentQuizAttempt> getQuizAttemptsByStatus(Long quizId, AttemptStatus status) {
        return attemptRepository.findByQuizIdAndStatus(quizId, status);
    }

    // ==================== QUIZ STATISTICS ====================

    /**
     * Get detailed statistics for a quiz
     */
    public Map<String, Object> getQuizStatistics(Long quizId) {
        List<StudentQuizAttempt> allAttempts = attemptRepository.findByQuizId(quizId);
        List<StudentQuizAttempt> submittedAttempts = attemptRepository.findByQuizIdAndStatus(quizId, AttemptStatus.SUBMITTED);

        Map<String, Object> stats = new HashMap<>();

        // Basic counts
        stats.put("totalAttempts", allAttempts.size());
        stats.put("submittedAttempts", submittedAttempts.size());
        stats.put("inProgressAttempts", allAttempts.size() - submittedAttempts.size());

        if (!submittedAttempts.isEmpty()) {
            // Calculate average score
            double averageScore = submittedAttempts.stream()
                    .mapToInt(StudentQuizAttempt::getScore)
                    .average()
                    .orElse(0.0);
            stats.put("averageScore", Math.round(averageScore * 100.0) / 100.0);

            // Calculate completion rate
            double completionRate = (submittedAttempts.size() * 100.0) / allAttempts.size();
            stats.put("completionRate", Math.round(completionRate * 100.0) / 100.0);

            // Find highest and lowest scores
            int highestScore = submittedAttempts.stream()
                    .mapToInt(StudentQuizAttempt::getScore)
                    .max()
                    .orElse(0);
            int lowestScore = submittedAttempts.stream()
                    .mapToInt(StudentQuizAttempt::getScore)
                    .min()
                    .orElse(0);
            stats.put("highestScore", highestScore);
            stats.put("lowestScore", lowestScore);

            // Calculate pass percentage (assuming 40% is passing)
            Quiz quiz = quizRepository.findById(quizId).orElse(null);
            if (quiz != null) {
                int passingMarks = (int) (quiz.getTotalMarks() * 0.4);
                long passedCount = submittedAttempts.stream()
                        .filter(attempt -> attempt.getScore() >= passingMarks)
                        .count();
                double passPercentage = (passedCount * 100.0) / submittedAttempts.size();
                stats.put("passPercentage", Math.round(passPercentage * 100.0) / 100.0);
            }
        } else {
            stats.put("averageScore", 0);
            stats.put("completionRate", 0);
            stats.put("highestScore", 0);
            stats.put("lowestScore", 0);
            stats.put("passPercentage", 0);
        }

        return stats;
    }

    // ==================== CREATE QUIZ ====================

    /**
     * Create a new quiz with questions
     */
    public Quiz createQuiz(Quiz quiz, List<QuizQuestion> questions) {

        // Save quiz first
        Quiz savedQuiz = quizRepository.save(quiz);

        // Link each question to this quiz
        for (QuizQuestion q : questions) {
            q.setQuiz(savedQuiz);
        }

        // Save all questions
        questionRepository.saveAll(questions);

        return savedQuiz;
    }

    // ==================== DELETE QUIZ ====================

    /**
     * Delete a quiz and all related data (cascading delete)
     */
    public void deleteQuiz(Long quizId) {
        // Check if quiz exists
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        // Delete all attempts first (which will cascade to answers)
        List<StudentQuizAttempt> attempts = attemptRepository.findByQuizId(quizId);
        attemptRepository.deleteAll(attempts);

        // Delete all questions
        List<QuizQuestion> questions = questionRepository.findByQuizId(quizId);
        questionRepository.deleteAll(questions);

        // Finally delete the quiz
        quizRepository.delete(quiz);
    }
}