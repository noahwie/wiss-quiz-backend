package com.wiss.quizbackend.service;

import com.wiss.quizbackend.entity.GameSession;
import com.wiss.quizbackend.repository.GameSessionRepository;
import com.wiss.quizbackend.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final AppUserRepository appUserRepository;

    public GameSessionService(GameSessionRepository gameSessionRepository, AppUserRepository appUserRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * Startet ein neues Quiz-Game für einen User
     */
    @Transactional
    public GameSession startGame(Long userId, String category, int totalQuestions) {

        // Validierung - User existiert?
        if (!appUserRepository.existsById(userId)) {
            throw new IllegalArgumentException("User mit ID " + userId + " existiert nicht!");
        }

        // Validierung - Kategorie gültig?
        if (!isValidCategory(category)) {
            throw new IllegalArgumentException(
                    "Ungültige Kategorie: " + category +
                            ". Erlaubt: sports, math, geography, science, history, movies, games"
            );
        }

        // Validierung - totalQuestions sinnvoll?
        if (totalQuestions < 1 || totalQuestions > 50) {
            throw new IllegalArgumentException(
                    "totalQuestions muss zwischen 1 und 50 sein!"
            );
        }

        // GameSession erstellen
        GameSession session = new GameSession();
        session.setUserId(userId);
        session.setCategory(category);
        session.setTotalQuestions(totalQuestions);
        session.setCorrectAnswers(0);
        session.setTotalScore(0);
        session.setPlayedAt(LocalDateTime.now());

        GameSession saved = gameSessionRepository.save(session);

        System.out.println("🎮 Game gestartet: ID=" + saved.getId() +
                ", User=" + userId + ", Kategorie=" + category);

        return saved;
    }

    /**
     * Beendet ein Game und berechnet den finalen Score
     */
    @Transactional
    public GameSession finishGame(Long sessionId, int correctAnswers) {

        // GameSession laden
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "GameSession mit ID " + sessionId + " nicht gefunden!"
                ));

        // Validierung - correctAnswers sinnvoll?
        if (correctAnswers < 0 || correctAnswers > session.getTotalQuestions()) {
            throw new IllegalArgumentException(
                    "correctAnswers muss zwischen 0 und " +
                            session.getTotalQuestions() + " sein!"
            );
        }

        // Score berechnen
        int score = calculateScore(correctAnswers);

        // GameSession aktualisieren
        session.setCorrectAnswers(correctAnswers);
        session.setTotalScore(score);

        GameSession updated = gameSessionRepository.save(session);

        System.out.println("🏆 Game beendet: ID=" + sessionId +
                ", Score=" + score + " (" + correctAnswers + "/" +
                session.getTotalQuestions() + " richtig)");

        return updated;
    }

    /**
     * Berechnet den Score basierend auf richtigen Antworten
     */
    private int calculateScore(int correctAnswers) {
        return correctAnswers * 10;
    }

    /**
     * Prüft ob die Kategorie gültig ist
     */
    private boolean isValidCategory(String category) {
        return List.of("sports", "math", "geography", "science",
                "history", "movies", "games").contains(category.toLowerCase());
    }

    /**
     * Lädt ein Game basierend auf deren ID.
     * @param sessionId
     * @return
     */
    public GameSession getGameById(Long sessionId) {
        return gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "GameSession mit ID " + sessionId + " nicht gefunden!"
                ));
    }

}