package com.wiss.quizbackend.controller;

import com.wiss.quizbackend.entity.AppUser;
import com.wiss.quizbackend.entity.GameSession;
import com.wiss.quizbackend.service.AppUserService;
import com.wiss.quizbackend.service.GameSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/game")
public class GameSessionController {

    private final GameSessionService gameSessionService;
    private final AppUserService appUserService;

    public GameSessionController(GameSessionService gameSessionService, AppUserService appUserService) {
        this.gameSessionService = gameSessionService;
        this.appUserService = appUserService;
    }

    /**
     * POST /api/game/start
     * Startet ein neues Quiz-Game
     */
    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public GameSession startGame(@RequestBody GameSession gameSession, @AuthenticationPrincipal UserDetails userDetails) {
        // Get the User from the request
        AppUser user = appUserService.findByUsername(userDetails.getUsername()).orElseThrow();

        return gameSessionService.startGame(user.getId(), gameSession.getCategory(), gameSession.getTotalQuestions());
    }

    /**
     * PUT /api/game/{sessionId}/finish
     * Beendet ein Game mit Resultat
     */
    @PutMapping("/{sessionId}/finish")
    public GameSession finishGame(@PathVariable Long sessionId,
                                  @RequestBody GameSession gameSession) {

        return gameSessionService.finishGame(sessionId, gameSession.getCorrectAnswers());
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
