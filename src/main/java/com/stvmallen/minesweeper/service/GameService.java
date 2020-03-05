package com.stvmallen.minesweeper.service;

import com.stvmallen.minesweeper.model.GameBean;
import com.stvmallen.minesweeper.model.NewGameRequest;

public interface GameService {
	GameBean createNewGame(NewGameRequest request);

	GameBean findGame(Long gameId);
}
