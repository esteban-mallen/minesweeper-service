package com.stvmallen.minesweeper.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.stvmallen.minesweeper.exception.InvalidCellException;
import com.stvmallen.minesweeper.exception.MineExplodedException;
import com.stvmallen.minesweeper.model.CellBean;
import com.stvmallen.minesweeper.model.GameBean;
import com.stvmallen.minesweeper.model.NewGameRequest;
import com.stvmallen.minesweeper.types.CellStatus;

@Service
@Slf4j
@RequiredArgsConstructor
public class CellServiceImpl implements CellService {
	@Override
	public void revealCell(GameBean gameBean, Long cellId) throws MineExplodedException {
		CellBean cellToReveal = gameBean.getCells()
			.stream()
			.filter(cellBean -> cellBean.getId().equals(cellId))
			.findFirst()
			.orElseThrow(() -> new InvalidCellException(String.format("No cell with cellId=%s found for gameId=%s", cellId, gameBean.getId())));

		cellToReveal.setCellStatus(CellStatus.REVEALED);

		if (cellToReveal.isMine()) {
			throw new MineExplodedException(String.format("Mine found in cellId=%s gameId=%s", cellId, gameBean.getId()));
		} else {
			checkAdjacentCells(gameBean, cellToReveal);
		}
	}

	@Override
	public void markCell(GameBean gameBean, Long cellId) {
		changeCellStatus(gameBean, cellId, CellStatus.MARKED);
	}

	@Override
	public void flagCell(GameBean gameBean, Long cellId) {
		changeCellStatus(gameBean, cellId, CellStatus.FLAGGED);
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

	private void changeCellStatus(GameBean gameBean, Long cellId, CellStatus cellStatus) {
		CellBean cell = gameBean.getCells()
			.stream()
			.filter(cellBean -> cellBean.getId().equals(cellId))
			.findFirst()
			.orElseThrow(() -> new InvalidCellException(String.format("No cell with cellId=%s found for gameId=%s", cellId, gameBean.getId())));

		cell.setCellStatus(cellStatus);
	}

	private void checkAdjacentCells(GameBean gameBean, CellBean cellToReveal) {
		cellToReveal.setCellStatus(CellStatus.REVEALED);
		Long revealedCellRow = cellToReveal.getRowPosition();
		Long revealedCellColumn = cellToReveal.getColumnPosition();

		List<CellBean> adjacentHiddenCells = gameBean.getCells()
			.stream()
			.filter(cellBean -> CellStatus.REVEALED != cellBean.getCellStatus()) //Ignore revealed cells
			.filter(cellBean -> cellBean.getRowPosition().equals(revealedCellRow) || cellBean.getRowPosition().equals(revealedCellRow - 1) || cellBean.getRowPosition().equals(revealedCellRow + 1))
			.filter(cellBean -> cellBean.getColumnPosition().equals(revealedCellColumn) || cellBean.getColumnPosition().equals(revealedCellColumn - 1) || cellBean.getColumnPosition().equals(revealedCellColumn + 1))
			.collect(Collectors.toList());

		long adjacentHiddenMines = adjacentHiddenCells.stream()
			.filter(CellBean::isMine)
			.count();

		if (adjacentHiddenMines != 0) {
			cellToReveal.setAdjacentMineCount(adjacentHiddenMines);
		} else {
			adjacentHiddenCells.forEach(cellBean -> checkAdjacentCells(gameBean, cellBean));
		}
	}
}
