package com.wiss.quizbackend.repository;

import com.wiss.quizbackend.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameSessionRepository  extends JpaRepository<GameSession, Long> {
    List<GameSession> findByUserId(Long userId);

    List<GameSession> findByCategory(Long category);

    List<GameSession> findByUserIdOrderByPlayedAtDesc(Long userId);

    List<GameSession> findByUserIdAndCategory(Long userId, Long category);
}
