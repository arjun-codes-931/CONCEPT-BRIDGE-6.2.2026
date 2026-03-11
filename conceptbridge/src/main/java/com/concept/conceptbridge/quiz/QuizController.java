package com.concept.conceptbridge.quiz;

import com.concept.conceptbridge.quiz.dto.AnswerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/student/quiz")
@CrossOrigin
public class QuizController {
    @Autowired
    private StudentQuizAttemptRepository attemptRepository;
    @Autowired
    private QuizService quizService;

    // ===============================
    // LIST QUIZZES
    // ===============================
    @GetMapping
    public List<Quiz> getQuizzes(
            @RequestParam String branch,
            @RequestParam Integer semester,
            @RequestParam String section
    ) {
        return quizService.getAvailableQuizzes(branch, semester, section);
    }

    @GetMapping("/{quizId}/questions")
    public List<QuizQuestion> getQuestions(@PathVariable Long quizId) {
        return quizService.getQuestionsByQuizId(quizId);
    }

    // ===============================
    // START QUIZ
    // ===============================
    @PostMapping("/{quizId}/start")
    public StudentQuizAttempt startQuiz(
            @PathVariable Long quizId,
            @RequestParam Long studentId
    ) {
        return quizService.startQuiz(studentId, quizId);
    }

    // ===============================
    // SUBMIT QUIZ
    // ===============================
    @PostMapping("/submit/{attemptId}")
    public ResponseEntity<Map<String, Object>> submitQuiz(
            @PathVariable Long attemptId,
            @RequestBody List<AnswerDTO> answers) {

        StudentQuizAttempt attempt = quizService.submitQuiz(attemptId, answers);

        // Return simple map to avoid circular JSON
        Map<String, Object> result = new HashMap<>();
        result.put("id", attempt.getId());
        result.put("studentId", attempt.getStudentId());
        result.put("score", attempt.getScore());
        result.put("status", attempt.getStatus().name());
        result.put("submittedAt", attempt.getSubmittedAt());

        return ResponseEntity.ok(result);
    }

    // ===============================
    // RESULT
    // ===============================
    @GetMapping("/result/{attemptId}")
    public StudentQuizAttempt getResult(@PathVariable Long attemptId) {
        return quizService.getQuizResult(attemptId);
    }
    @GetMapping("/attempt/{attemptId}/answers")
    public List<AnswerDTO> getSavedAnswers(@PathVariable Long attemptId) {

        return quizService.getSavedAnswers(attemptId);

    }
    @PostMapping("/attempt/{attemptId}/answer")
    public ResponseEntity<?> saveAnswer(
            @PathVariable Long attemptId,
            @RequestBody AnswerDTO answer) {

        quizService.saveAnswer(attemptId, answer);

        return ResponseEntity.ok().build();
    }
    @GetMapping("/{quizId}/details")
    public Quiz getQuizDetails(@PathVariable Long quizId) {
        return quizService.getQuizById(quizId);
    }
    @GetMapping("/status/{quizId}")
    public ResponseEntity<Map<String, Object>> getQuizStatus(
            @PathVariable Long quizId,
            @RequestParam Long studentId) {

        Optional<StudentQuizAttempt> attempt =
                attemptRepository.findByStudentIdAndQuizId(studentId, quizId);

        Map<String, Object> response = new HashMap<>();

        if (attempt.isPresent()) {

            StudentQuizAttempt a = attempt.get();

            response.put("hasAttempted", true);
            response.put("status", a.getStatus().name());
            response.put("score", a.getScore());
            response.put("attemptId", a.getId());   // ⭐ THIS LINE WAS MISSING

        } else {

            response.put("hasAttempted", false);

        }

        return ResponseEntity.ok(response);
    }
}