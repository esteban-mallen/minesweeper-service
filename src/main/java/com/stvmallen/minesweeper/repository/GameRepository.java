package com.stvmallen.minesweeper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stvmallen.minesweeper.entity.Game;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
}