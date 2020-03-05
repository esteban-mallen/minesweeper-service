package com.stvmallen.minesweeper.service;

import java.util.List;

import com.stvmallen.minesweeper.exception.MineExplodedException;
import com.stvmallen.minesweeper.model.CellBean;
import com.stvmallen.minesweeper.model.GameBean;
import com.stvmallen.minesweeper.model.NewGameRequest;

public interface CellService {
	/**
	 * Reveals a given cell, adding to its data the amount of adjacent mines. If none,
	 * adjacent cells will be revealed too.
	 *
	 * @param gameBean the game bean
	 * @param cellId   the id of the cell to reveal
	 * @throws MineExplodedException if the revealed cell contains a mine, this exception will be thrown
	 */
	void revealCell(GameBean gameBean, Long cellId) throws MineExplodedException;

	/**
	 * Marks a cell as potentially mined
	 *
	 * @param gameBean the game bean
	 * @param cellId   the cell id to mark
	 */
	void markCell(GameBean gameBean, Long cellId);

	/**
	 * Flags a cell as a mine
	 *
	 * @param gameBean the game bean
	 * @param cellId   the cell id to flag
	 */
	void flagCell(GameBean gameBean, Long cellId);

	/**
	 * Creates the cell matrix with the given parameters, adding mines in random positions
	 *
	 * @param request the cell matrix parameteres
	 * @return the list of cells created
	 */
	List<CellBean> createCells(NewGameRequest request);
}
