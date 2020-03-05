package com.stvmallen.minesweeper.service;

import com.stvmallen.minesweeper.model.GameBean;
import com.stvmallen.minesweeper.model.NewGameRequest;

public interface GameService {
	/**
	 * Creates a new game with the given parameters
	 *
	 * @param request contains the amount of rows, columns and mines
	 * @return the game bean
	 */
	GameBean createNewGame(NewGameRequest request);

	/**
	 * Finds a game by its id
	 *
	 * @param gameId the game id
	 * @return the game bean
	 */
	GameBean findGame(Long gameId);

	/**
	 * Reveals a given cell for a given game
	 *
	 * @param gameId the game id to which the cell belongs
	 * @param cellId the cell id
	 * @return the game bean
	 */
	GameBean revealCell(Long gameId, Long cellId);

	/**
	 * Pauses a given game
	 *
	 * @param gameId the game id
	 * @return the game bean
	 */
	GameBean pauseGame(Long gameId);

	/**
	 * Resumes a paused game
	 *
	 * @param gameId the game id
	 * @return the game bean
	 */
	GameBean resumeGame(Long gameId);

	/**
	 * Marks a given cell as possible mine
	 *
	 * @param gameId the game id
	 * @param cellId the cell id to flag
	 * @return the game bean
	 */
	GameBean markCell(Long gameId, Long cellId);

	/**
	 * Flags a given cell as mine
	 *
	 * @param gameId the game id
	 * @param cellId the cell id to flag
	 * @return the game bean
	 */
	GameBean flagCell(Long gameId, Long cellId);
}
