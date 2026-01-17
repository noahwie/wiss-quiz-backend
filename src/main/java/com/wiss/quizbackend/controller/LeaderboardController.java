package com.wiss.quizbackend.controller;

import com.wiss.quizbackend.dto.LeaderboardDTO;
import com.wiss.quizbackend.entity.AppUser;
import com.wiss.quizbackend.service.AppUserService;
import com.wiss.quizbackend.service.LeaderboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;
    private final AppUserService appUserService;


    // Constructor Injection
    public LeaderboardController(LeaderboardService leaderboardService,AppUserService appUserService) {
        this.leaderboardService = leaderboardService;
        this.appUserService = appUserService;
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
    @GetMapping("/user/stats")
    public Map<String, Object> getUserStats(@AuthenticationPrincipal UserDetails userDetails) {

        AppUser user = appUserService.findByUsername(userDetails.getUsername()).orElseThrow();

        return leaderboardService.getUserStats(user.getId());
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
