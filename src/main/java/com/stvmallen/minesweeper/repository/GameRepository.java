package com.stvmallen.minesweeper.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stvmallen.minesweeper.entity.Game;
import com.stvmallen.minesweeper.types.GameStatus;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
	Optional<Game> findByIdAndStatusIn(Long gameId, GameStatus... gameStatus);
}
