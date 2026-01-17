package com.wiss.quizbackend.repository;

import com.wiss.quizbackend.entity.GameSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für Leaderboard-Funktionen.
 * Dieses Repository ist spezialisiert auf aggregierte Daten
 * und Statistiken für das Leaderboard.
 * Verwendet GameSession als Entity, aber nur für Leaderboard-Zwecke!
 */
@Repository
public interface LeaderboardRepository extends JpaRepository<GameSession, Long> {

    /**
     * Top 10 Spieler (global)
     * Gruppiert alle GameSessions nach User und summiert den Score.
     *
     * @param pageable Für LIMIT (PageRequest.of(0, 10))
     * @return Array: [userId, totalScore, gamesPlayed]
     */
    @Query("""
        SELECT g.userId, SUM(g.totalScore) as totalScore, COUNT(g.id) as gamesPlayed
        FROM GameSession g
        GROUP BY g.userId
        ORDER BY totalScore DESC
        """)
    List<Object[]> findTop10Players(Pageable pageable);

    /**
     * Top 10 Spieler nach Kategorie.
     * Wie findTop10Players(), aber nur für eine bestimmte Kategorie.
     *
     * @param category Die Kategorie (z.B. "sports")
     * @param pageable Für LIMIT
     * @return Array: [userId, totalScore, gamesPlayed]
     */
    @Query("""
        SELECT g.userId, SUM(g.totalScore) as totalScore, COUNT(g.id) as gamesPlayed
        FROM GameSession g
        WHERE g.category = :category
        GROUP BY g.userId
        ORDER BY totalScore DESC
        """)
    List<Object[]> findTop10PlayersByCategory(
            @Param("category") String category,
            Pageable pageable
    );

    /**
     * Zählt die Anzahl Games eines Users
     *
     * @param userId Die User-ID
     * @return Anzahl gespielte Games
     */
    @Query("SELECT COUNT(g) FROM GameSession g WHERE g.userId = :userId")
    Long countGamesByUser(@Param("userId") Long userId);

    /**
     * Summiert den Total-Score eines Users
     *
     * @param userId Die User-ID
     * @return Total Score über alle Games
     */
    @Query("SELECT SUM(g.totalScore) FROM GameSession g WHERE g.userId = :userId")
    Integer sumScoreByUser(@Param("userId") Long userId);

    /**
     * Berechnet den durchschnittlichen Score eines Users
     *
     * @param userId Die User-ID
     * @return Durchschnittlicher Score
     */
    @Query("SELECT AVG(g.totalScore) FROM GameSession g WHERE g.userId = :userId")
    Double averageScoreByUser(@Param("userId") Long userId);

    /**
     * Anzahl Games pro Kategorie.
     * Zeigt welche Kategorien am beliebtesten sind.
     *
     * @return Array: [category, count]
     */
    @Query("""
        SELECT g.category, COUNT(g.id) as count
        FROM GameSession g
        GROUP BY g.category
        ORDER BY count DESC
        """)
    List<Object[]> countGamesByCategory();
}