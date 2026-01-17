package com.wiss.quizbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name= "game_sessions")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 64)
    private String category;

    @Column(nullable = false)
    private Integer correctAnswers;

    @Column(nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Integer totalScore;

    @Column(nullable = false)
    private LocalDateTime playedAt;

    public GameSession() {}

    public GameSession(Long userId, String category, Integer correctAnswers, Integer totalQuestions, Integer totalScore) {
        this.userId = userId;
        this.category = category;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.totalScore = totalScore;
        this.playedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }
}
