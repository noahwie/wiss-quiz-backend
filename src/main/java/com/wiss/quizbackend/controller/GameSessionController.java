package com.wiss.quizbackend.controller;

import com.wiss.quizbackend.entity.GameSession;
import com.wiss.quizbackend.service.GameSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameSessionController {

    private final GameSessionService gameSessionService;

    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    /**
     * POST /api/game/start
     * Startet ein neues Quiz-Game
     */
    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public GameSession startGame(@RequestParam Long userId,
                                 @RequestParam String category,
                                 @RequestParam(defaultValue = "10") int totalQuestions) {
        return gameSessionService.startGame(userId, category, totalQuestions);
    }

    /**
     * PUT /api/game/{sessionId}/finish
     * Beendet ein Game mit Resultat
     */
    @PutMapping("/{sessionId}/finish")
    public GameSession finishGame(@PathVariable Long sessionId,
                                  @RequestParam int correctAnswers) {
        return gameSessionService.finishGame(sessionId, correctAnswers);
    }

    /**
     * GET /api/game/{sessionId}
     * Lädt eine bestimmte GameSession
     */
    @GetMapping("/{sessionId}")
    public GameSession getGameById(@PathVariable Long sessionId) {
        return gameSessionService.getGameById(sessionId);
    }
}