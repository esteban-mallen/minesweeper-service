package com.stvmallen.minesweeper.service;

import java.util.List;

import com.stvmallen.minesweeper.model.CellBean;
import com.stvmallen.minesweeper.model.NewGameRequest;

public interface CellService {
	CellBean revealCell(Long cellId);

	CellBean markCell(Long cellId);

	CellBean flagCell(Long cellId);

	List<CellBean> createCells(NewGameRequest request);
}
