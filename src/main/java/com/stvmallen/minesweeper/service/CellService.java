package com.stvmallen.minesweeper.service;

import com.stvmallen.minesweeper.model.CellBean;

public interface CellService {
	CellBean revealCell(Long cellId);

	CellBean markCell(Long cellId);

	CellBean flagCell(Long cellId);
}
