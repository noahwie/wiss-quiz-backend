package com.wiss.quizbackend.controller;

import com.wiss.quizbackend.dto.LeaderboardDTO;
import com.wiss.quizbackend.service.LeaderboardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    // Constructor Injection
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    /**
     * GET /api/leaderboard/top10
     * Lädt die Top 10 Spieler (Global)
     */
    @GetMapping("/top10")
    public List<LeaderboardDTO> getTop10Players() {
        return leaderboardService.getTop10Players();
    }

    /**
     * GET /api/leaderboard/top10/sports
     * Lädt die Top 10 Spieler einer Kategorie
     */
    @GetMapping("/top10/{category}")
    public List<LeaderboardDTO> getTop10ByCategory(@PathVariable String category) {
        return leaderboardService.getTop10PlayersByCategory(category);
    }

    /**
     * GET /api/leaderboard/user/1/stats
     * Lädt Statistiken eines Users
     */
    @GetMapping("/user/{userId}/stats")
    public Map<String, Object> getUserStats(@PathVariable Long userId) {
        return leaderboardService.getUserStats(userId);
    }

    /**
     * GET /api/leaderboard/categories
     * Lädt Statistiken zu allen Kategorien
     */
    @GetMapping("/categories")
    public List<Map<String, Object>> getCategoryStats() {
        return leaderboardService.getCategoryStats();
    }
}