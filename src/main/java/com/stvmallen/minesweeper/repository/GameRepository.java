package com.stvmallen.minesweeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stvmallen.minesweeper.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
