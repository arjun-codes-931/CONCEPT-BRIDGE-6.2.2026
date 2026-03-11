package com.concept.conceptbridge.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import java.util.List;

@Entity
@Table(
        name = "student_quiz_attempts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "quiz_id"})
        }
)
public class StudentQuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonIgnore
    private Quiz quiz;

    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;

    private Integer score;

    @Enumerated(EnumType.STRING)
    private AttemptStatus status;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StudentQuizAnswer> answers;

    public StudentQuizAttempt() {}

    public StudentQuizAttempt(Long studentId, Quiz quiz) {
        this.studentId = studentId;
        this.quiz = quiz;
        this.startedAt = LocalDateTime.now();
        this.status = AttemptStatus.IN_PROGRESS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public AttemptStatus getStatus() {
        return status;
    }

    public void setStatus(AttemptStatus status) {
        this.status = status;
    }

    public List<StudentQuizAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<StudentQuizAnswer> answers) {
        this.answers = answers;
    }

    // getters & setters
}