package com.stvmallen.minesweeper.service;

import java.util.List;

import com.stvmallen.minesweeper.exception.MineExplodedException;
import com.stvmallen.minesweeper.model.CellBean;
import com.stvmallen.minesweeper.model.GameBean;
import com.stvmallen.minesweeper.model.NewGameRequest;

public interface CellService {
	void revealCell(GameBean gameBean, Long cellId) throws MineExplodedException;

	void markCell(GameBean gameBean, Long cellId);

	void flagCell(GameBean gameBean, Long cellId);

	List<CellBean> createCells(NewGameRequest request);
}
