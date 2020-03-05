package com.stvmallen.minesweeper.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.stvmallen.minesweeper.model.CellBean;
import com.stvmallen.minesweeper.model.NewGameRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class CellServiceImpl implements CellService {
	@Override
	public CellBean revealCell(Long cellId) {
		return null;
	}

	@Override
	public CellBean markCell(Long cellId) {
		return null;
	}

	@Override
	public CellBean flagCell(Long cellId) {
		return null;
	}

	@Override
	public List<CellBean> createCells(NewGameRequest request) {
		Preconditions.checkNotNull(request);
		Preconditions.checkNotNull(request.getColumnCount());
		Preconditions.checkNotNull(request.getRowCount());
		Preconditions.checkNotNull(request.getMineCount());

		long totalRows = request.getRowCount();
		long totalColumns = request.getColumnCount();
		long totalMines = request.getMineCount();

		Preconditions.checkArgument(totalColumns > 0, "Column count should be greater than 0");
		Preconditions.checkArgument(totalRows > 0, "Row count should be greater than 0");
		Preconditions.checkArgument(totalMines >= 0, "Mine count should be a positive number");
		Preconditions.checkArgument((totalColumns * totalRows) >= totalMines, "Mine count can't be greater than total cells");

		List<CellBean> cells = generateCells(totalRows, totalColumns);
		addMines(cells, totalMines);

		return cells;
	}

	private List<CellBean> generateCells(long totalRows, long totalColumns) {
		List<CellBean> cells = Lists.newArrayList();

		for (long row = 0; row < totalRows; row++) {
			for (long column = 0; column < totalColumns; column++) {
				cells.add(CellBean.builder()
					.rowPosition(row)
					.columnPosition(column)
					.build());
			}
		}

		return cells;
	}

	private void addMines(List<CellBean> cells, long totalMines) {
		Set<Integer> minedCellIndexes = Sets.newHashSet();

		while (minedCellIndexes.size() < totalMines) {
			minedCellIndexes.add(ThreadLocalRandom.current().nextInt(cells.size() - 1));
		}

		minedCellIndexes
			.stream()
			.map(cells::get)
			.forEach(cellToMine -> cellToMine.setMine(true));
	}
}
