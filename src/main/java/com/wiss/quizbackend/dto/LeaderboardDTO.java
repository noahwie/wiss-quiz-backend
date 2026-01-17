package com.wiss.quizbackend.dto;

/**
 * Data Transfer Object für Leaderboard-Einträge
 *
 * Wird verwendet, um Leaderboard-Daten ans Frontend zu senden.
 * Kombiniert User-Daten mit GameSession-Statistiken.
 */
public class LeaderboardDTO {

    private Long userId;
    private String username;
    private Long gamesPlayed;
    private Long totalScore;
    private String category;  // Optional: Für Kategorie-spezifisches Leaderboard

    // Default Constructor
    public LeaderboardDTO() {}

    // Constructor für Global Leaderboard
    public LeaderboardDTO(Long userId, String username,
                          Long gamesPlayed, Long totalScore) {
        this.userId = userId;
        this.username = username;
        this.gamesPlayed = gamesPlayed;
        this.totalScore = totalScore;
    }

    // Constructor für Kategorie-Leaderboard
    public LeaderboardDTO(Long userId, String username,
                          Long gamesPlayed, Long totalScore,
                          String category) {
        this.userId = userId;
        this.username = username;
        this.gamesPlayed = gamesPlayed;
        this.totalScore = totalScore;
        this.category = category;
    }

    // Getter und Setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(Long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "LeaderboardDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", gamesPlayed=" + gamesPlayed +
                ", totalScore=" + totalScore +
                ", category='" + category + '\'' +
                '}';
    }

}