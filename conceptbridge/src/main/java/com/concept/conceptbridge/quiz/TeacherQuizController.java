package com.concept.conceptbridge.quiz;

import com.concept.conceptbridge.quiz.Quiz;
import com.concept.conceptbridge.quiz.StudentQuizAttempt;
import com.concept.conceptbridge.quiz.dto.QuizCreationDTO;
import com.concept.conceptbridge.teacher.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher/quiz")
public class TeacherQuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private TeacherQuizService teacherQuizService;

    @Autowired
    private TeacherProfileService teacherProfileService;

    // ==================== GET QUIZZES ====================

    /**
     * Get all quizzes created by a teacher
     * GET /teacher/quiz?teacherId=123
     */
    @GetMapping
    public ResponseEntity<List<Quiz>> getTeacherQuizzes(
            @RequestParam String teacherUsername,
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) String subject) {

        try {
            // Get numeric user ID from username (for createdBy field)
            Long userId = teacherProfileService.getUserIdByUsername(teacherUsername);

            if (userId == null) {
                return ResponseEntity.status(404)
                        .body(Collections.emptyList());
            }

            List<Quiz> quizzes;
            if (branch != null || subject != null) {
                quizzes = teacherQuizService.getQuizzesByTeacherWithFilters(userId, branch, subject);
            } else {
                quizzes = teacherQuizService.getQuizzesByTeacher(userId);
            }

            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== GET QUIZ DETAILS ====================

    /**
     * Get quiz details with questions
     * GET /teacher/quiz/{quizId}
     */
    @GetMapping("/{quizId}")
    public ResponseEntity<Map<String, Object>> getQuizDetails(@PathVariable Long quizId) {
        try {
            Map<String, Object> quizDetails = teacherQuizService.getQuizDetails(quizId);
            return ResponseEntity.ok(quizDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== GET QUIZ ATTEMPTS ====================

    /**
     * Get all student attempts for a quiz
     * GET /teacher/quiz/{quizId}/attempts
     */
    @GetMapping("/{quizId}/attempts")
    public ResponseEntity<List<StudentQuizAttempt>> getQuizAttempts(@PathVariable Long quizId) {
        List<StudentQuizAttempt> attempts = teacherQuizService.getQuizAttempts(quizId);
        return ResponseEntity.ok(attempts);
    }

    // ==================== GET QUIZ STATISTICS ====================

    /**
     * Get statistics for a quiz
     * GET /teacher/quiz/{quizId}/stats
     */
    @GetMapping("/{quizId}/stats")
    public ResponseEntity<Map<String, Object>> getQuizStatistics(@PathVariable Long quizId) {
        try {
            Map<String, Object> stats = teacherQuizService.getQuizStatistics(quizId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Quiz not found");
            return ResponseEntity.badRequest().body(error);
        }
    }
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<QuizQuestion>> getQuizQuestions(@PathVariable Long quizId) {
        try {
            List<QuizQuestion> questions = quizService.getQuestionsByQuizId(quizId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    // ==================== CREATE QUIZ ====================

    /**
     * Create a new quiz
     * POST /teacher/quiz
     */
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody QuizCreationDTO quizCreationDTO) {
        try {
            Quiz createdQuiz = teacherQuizService.createQuiz(
                    quizCreationDTO.getQuiz(),
                    quizCreationDTO.getQuestions()
            );

            return ResponseEntity.ok(createdQuiz);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== DELETE QUIZ ====================

    /**
     * Delete a quiz
     * DELETE /teacher/quiz/{quizId}
     */
    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long quizId) {
        try {
            teacherQuizService.deleteQuiz(quizId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



    // ==================== HEALTH CHECK ====================

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Teacher Quiz Controller is working!");
    }
}