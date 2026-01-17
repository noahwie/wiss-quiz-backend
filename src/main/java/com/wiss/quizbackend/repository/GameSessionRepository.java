package com.wiss.quizbackend.repository;

import com.wiss.quizbackend.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für GameSession CRUD-Operationen
 *
 * Zuständig für:
 * - GameSession speichern/laden/löschen
 * - Einfache Queries nach User oder Kategorie
 *
 * Für Leaderboard-Funktionen siehe LeaderboardRepository (kommt später)!
 */
@Repository
public interface GameSessionRepository
        extends JpaRepository<GameSession, Long> {

    // Spring Boot generiert automatisch:
    // - save(GameSession) - CREATE/UPDATE
    // - findById(Long id) - READ by ID
    // - findAll() - READ all
    // - deleteById(Long id) - DELETE
    // - count() - COUNT
    // ... und viele mehr!

    /**
     * Findet alle GameSessions eines bestimmten Users
     *
     * SQL: SELECT * FROM game_sessions WHERE user_id = ?
     */
    List<GameSession> findByUserId(Long userId);

    /**
     * Findet alle GameSessions einer bestimmten Kategorie
     *
     * SQL: SELECT * FROM game_sessions WHERE category = ?
     */
    List<GameSession> findByCategory(String category);

    /**
     * Findet alle GameSessions eines Users, sortiert nach Datum (neueste zuerst)
     *
     * SQL: SELECT * FROM game_sessions WHERE user_id = ? ORDER BY played_at DESC
     */
    List<GameSession> findByUserIdOrderByPlayedAtDesc(Long userId);

    /**
     * Findet alle GameSessions eines Users in einer bestimmten Kategorie
     *
     * SQL: SELECT * FROM game_sessions WHERE user_id = ? AND category = ?
     */
    List<GameSession> findByUserIdAndCategory(Long userId, String category);

}