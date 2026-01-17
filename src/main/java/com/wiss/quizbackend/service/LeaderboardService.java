package com.wiss.quizbackend.service;

import com.wiss.quizbackend.dto.LeaderboardDTO;
import com.wiss.quizbackend.entity.AppUser;
import com.wiss.quizbackend.repository.LeaderboardRepository;
import com.wiss.quizbackend.repository.AppUserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;  // ← Leaderboard Repo!
    private final AppUserRepository appUserRepository;

    // Constructor Injection
    public LeaderboardService(LeaderboardRepository leaderboardRepository, AppUserRepository appUserRepository) {
        this.leaderboardRepository = leaderboardRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * Lädt die Top 10 Spieler (Global)
     *
     * @return Liste mit Top 10 Spielern sortiert nach Total Score
     */
    @Transactional(readOnly = true)
    public List<LeaderboardDTO> getTop10Players() {
        // Schritt 1: LeaderboardRepository aufrufen
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> results = leaderboardRepository.findTop10Players(pageable);

        // Schritt 2: Object[] zu DTO transformieren
        return transformToLeaderboardDTOs(results, null);
    }

    /**
     * Lädt die Top 10 Spieler einer bestimmten Kategorie
     *
     * @param category Die Kategorie (z.B. "sports")
     * @return Liste mit Top 10 Spielern in der Kategorie
     */
    @Transactional(readOnly = true)
    public List<LeaderboardDTO> getTop10PlayersByCategory(String category) {
        // Validierung
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Kategorie darf nicht leer sein!");
        }

        // LeaderboardRepository aufrufen
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> results = leaderboardRepository
                .findTop10PlayersByCategory(category, pageable);

        // Object[] zu DTO transformieren
        return transformToLeaderboardDTOs(results, category);
    }

    /**
     * Lädt die Statistik eines bestimmten Users
     *
     * @param userId Die User-ID
     * @return Map mit User-Statistiken
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getUserStats(Long userId) {
        // Validierung - User existiert?
        if (!appUserRepository.existsById(userId)) {
            throw new IllegalArgumentException("User mit ID " + userId + " existiert nicht!");
        }

        // Username laden
        String username = appUserRepository.findById(userId)
                .map(AppUser::getUsername)
                .orElse("Unknown User");

        // Statistiken aus LeaderboardRepository laden
        Long gamesPlayed = leaderboardRepository.countGamesByUser(userId);
        Integer totalScore = leaderboardRepository.sumScoreByUser(userId);
        Double averageScore = leaderboardRepository.averageScoreByUser(userId);

        // In Map packen
        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", userId);
        stats.put("username", username);
        stats.put("gamesPlayed", gamesPlayed != null ? gamesPlayed : 0);
        stats.put("totalScore", totalScore != null ? totalScore : 0);
        stats.put("averageScore", averageScore != null ? averageScore : 0.0);

        return stats;
    }

    /**
     * Lädt Statistiken zu allen Kategorien
     *
     * @return Liste mit Kategorie-Statistiken
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoryStats() {
        // LeaderboardRepository aufrufen
        List<Object[]> results = leaderboardRepository.countGamesByCategory();

        // Object[] zu Map transformieren
        List<Map<String, Object>> stats = new ArrayList<>();

        for (Object[] row : results) {
            String category = (String) row[0];
            Long count = ((Number) row[1]).longValue();

            Map<String, Object> categoryStats = new HashMap<>();
            categoryStats.put("category", category);
            categoryStats.put("gamesPlayed", count);

            stats.add(categoryStats);
        }

        return stats;
    }

    /**
     * Hilfsmethode: Transformiert Object[] zu LeaderboardDTO
     *
     * @param results Die Resultate aus dem Repository
     * @param category Optional: Kategorie für Kategorie-Leaderboard
     * @return Liste von LeaderboardDTOs
     */
    private List<LeaderboardDTO> transformToLeaderboardDTOs(
            List<Object[]> results,
            String category) {

        List<LeaderboardDTO> leaderboard = new ArrayList<>();

        for (Object[] row : results) {
            Long userId = ((Number) row[0]).longValue();
            Long totalScore = ((Number) row[1]).longValue();
            Long gamesPlayed = ((Number) row[2]).longValue();

            // Username laden
            String username = appUserRepository.findById(userId)
                    .map(AppUser::getUsername)
                    .orElse("Unknown User");

            // DTO erstellen
            LeaderboardDTO dto = new LeaderboardDTO(
                    userId,
                    username,
                    gamesPlayed,
                    totalScore,
                    category  // kann null sein für Global Leaderboard
            );

            leaderboard.add(dto);
        }

        return leaderboard;
    }
}