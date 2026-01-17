package com.wiss.quizbackend.repository;

import com.wiss.quizbackend.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository  extends JpaRepository<GameSession, Long> {

}
