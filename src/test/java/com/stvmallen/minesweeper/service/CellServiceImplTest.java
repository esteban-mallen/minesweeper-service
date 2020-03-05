package com.stvmallen.minesweeper.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.api.Condition;
import org.mockito.Answers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stvmallen.minesweeper.exception.InvalidCellException;
import com.stvmallen.minesweeper.exception.MineExplodedException;
import com.stvmallen.minesweeper.model.CellBean;
import com.stvmallen.minesweeper.model.GameBean;
import com.stvmallen.minesweeper.model.NewGameRequest;
import com.stvmallen.minesweeper.types.CellStatus;

public class CellServiceImplTest {
	private static final Predicate<CellBean> IS_MINE_PREDICATE = CellBean::isMine;
	private CellService cellService;

	@BeforeMethod
	public void setUp() {
		cellService = new CellServiceImpl();
	}

	@Test
	public void testCreateCells() {
		Long rows = 3L;
		Long columns = 3L;
		Long mines = 1L;

		List<CellBean> generatedCells = cellService.createCells(new NewGameRequest(rows, columns, mines));

		assertThat(generatedCells).hasSize(rows.intValue() * columns.intValue());
		assertThat(generatedCells).areExactly(mines.intValue(), new Condition<>(IS_MINE_PREDICATE, "Should have generated %d mines", mines));
	}

	@Test
	public void testCreateCells_invalidRowCount() {
		Long rows = 0L;
		Long columns = 3L;
		Long mines = 1L;

		assertThatThrownBy(() -> cellService.createCells(new NewGameRequest(rows, columns, mines)))
			.hasMessage("Row count should be greater than 0");
	}

	@Test
	public void testCreateCells_invalidColumnCount() {
		Long rows = 3L;
		Long columns = 0L;
		Long mines = 1L;

		assertThatThrownBy(() -> cellService.createCells(new NewGameRequest(rows, columns, mines)))
			.hasMessage("Column count should be greater than 0");
	}

	@Test
	public void testCreateCells_negativeMineCount() {
		Long rows = 3L;
		Long columns = 3L;
		Long mines = -1L;

		assertThatThrownBy(() -> cellService.createCells(new NewGameRequest(rows, columns, mines)))
			.hasMessage("Mine count should be a positive number");
	}

	@Test
	public void testCreateCells_invalidMineCount() {
		Long rows = 3L;
		Long columns = 3L;
		Long mines = 10L;

		assertThatThrownBy(() -> cellService.createCells(new NewGameRequest(rows, columns, mines)))
			.hasMessage("Mine count can't be greater than total cells");
	}

	@Test
	public void testMarkCell() {
		Long cellId = 1L;
		GameBean gameBean = mock(GameBean.class);
		CellBean cell = mock(CellBean.class);

		when(gameBean.getCells()).thenReturn(List.of(cell));
		when(cell.getId()).thenReturn(cellId);

		cellService.markCell(gameBean, cellId);

		verify(cell).setCellStatus(CellStatus.MARKED);
	}

	@Test
	public void testMarkCell_invalidCell() {
		Long cellId = 2L;
		GameBean gameBean = mock(GameBean.class);
		CellBean cell = mock(CellBean.class);

		when(gameBean.getCells()).thenReturn(List.of(cell));
		when(cell.getId()).thenReturn(1L);

		assertThatThrownBy(() -> cellService.markCell(gameBean, cellId))
			.isInstanceOf(InvalidCellException.class)
			.hasMessage(String.format("No cell with cellId=%s found for gameId=%s", cellId, gameBean.getId()));

		verify(cell, never()).setCellStatus(CellStatus.MARKED);
	}

	@Test
	public void testFlagCell() {
		Long cellId = 1L;
		GameBean gameBean = mock(GameBean.class);
		CellBean cell = mock(CellBean.class);

		when(gameBean.getCells()).thenReturn(List.of(cell));
		when(cell.getId()).thenReturn(cellId);

		cellService.flagCell(gameBean, cellId);

		verify(cell).setCellStatus(CellStatus.FLAGGED);
	}

	@Test
	public void testFlagCell_invalidCell() {
		Long cellId = 2L;
		GameBean gameBean = mock(GameBean.class);
		CellBean cell = mock(CellBean.class);

		when(gameBean.getCells()).thenReturn(List.of(cell));
		when(cell.getId()).thenReturn(1L);

		assertThatThrownBy(() -> cellService.flagCell(gameBean, cellId))
			.isInstanceOf(InvalidCellException.class)
			.hasMessage(String.format("No cell with cellId=%s found for gameId=%s", cellId, gameBean.getId()));

		verify(cell, never()).setCellStatus(CellStatus.FLAGGED);
	}

	@Test
	public void testRevealCell() throws MineExplodedException {
		Long cellId = 1L;
		GameBean gameBean = mock(GameBean.class);
		CellBean cell = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean adjacentCell1 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean adjacentCell2 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean adjacentCell3 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);

		when(gameBean.getCells()).thenReturn(List.of(cell, adjacentCell1, adjacentCell2, adjacentCell3));
		when(cell.getId()).thenReturn(cellId);
		when(cell.getRowPosition()).thenReturn(0L);
		when(cell.getColumnPosition()).thenReturn(0L);
		when(cell.isMine()).thenReturn(false);
		when(adjacentCell1.getRowPosition()).thenReturn(0L);
		when(adjacentCell1.getColumnPosition()).thenReturn(1L);
		when(adjacentCell2.getRowPosition()).thenReturn(1L);
		when(adjacentCell2.getColumnPosition()).thenReturn(0L);
		when(adjacentCell3.getRowPosition()).thenReturn(1L);
		when(adjacentCell3.getColumnPosition()).thenReturn(1L);
		when(adjacentCell3.isMine()).thenReturn(true);

		cellService.revealCell(gameBean, cellId);

		verify(cell, atLeastOnce()).setCellStatus(CellStatus.REVEALED);
		verify(cell).setAdjacentMineCount(1L);
	}

	@Test
	public void testRevealCell_testRecursivity() throws MineExplodedException {
		Long cellId = 1L;
		GameBean gameBean = mock(GameBean.class);
		CellBean cell1 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean cell2 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean cell3 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean cell4 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean cell5 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean cell6 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean cell7 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean cell8 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);
		CellBean cell9 = mock(CellBean.class, Answers.CALLS_REAL_METHODS);

		when(gameBean.getCells()).thenReturn(List.of(cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9));
		when(cell1.getId()).thenReturn(cellId);
		when(cell1.getRowPosition()).thenReturn(0L);
		when(cell1.getColumnPosition()).thenReturn(0L);
		when(cell2.getRowPosition()).thenReturn(0L);
		when(cell2.getColumnPosition()).thenReturn(1L);
		when(cell3.getRowPosition()).thenReturn(0L);
		when(cell3.getColumnPosition()).thenReturn(2L);
		when(cell4.getRowPosition()).thenReturn(1L);
		when(cell4.getColumnPosition()).thenReturn(0L);
		when(cell5.getRowPosition()).thenReturn(1L);
		when(cell5.getColumnPosition()).thenReturn(1L);
		when(cell6.getRowPosition()).thenReturn(1L);
		when(cell6.getColumnPosition()).thenReturn(2L);
		when(cell7.getRowPosition()).thenReturn(2L);
		when(cell7.getColumnPosition()).thenReturn(0L);
		when(cell8.getRowPosition()).thenReturn(2L);
		when(cell8.getColumnPosition()).thenReturn(1L);
		when(cell9.getRowPosition()).thenReturn(2L);
		when(cell9.getColumnPosition()).thenReturn(2L);
		when(cell9.isMine()).thenReturn(true);

		cellService.revealCell(gameBean, cellId);

		verify(cell1, atLeastOnce()).setCellStatus(CellStatus.REVEALED);
		verify(cell2, atLeastOnce()).setCellStatus(CellStatus.REVEALED);
		verify(cell4, atLeastOnce()).setCellStatus(CellStatus.REVEALED);

		verify(cell1, never()).setAdjacentMineCount(anyLong());
		verify(cell5, atLeastOnce()).setAdjacentMineCount(1L);
	}

	@Test
	public void testRevealCell_isMine() {
		Long cellId = 1L;
		GameBean gameBean = mock(GameBean.class);
		CellBean cell = mock(CellBean.class);

		when(gameBean.getCells()).thenReturn(List.of(cell));
		when(cell.getId()).thenReturn(cellId);
		when(cell.isMine()).thenReturn(true);

		assertThatThrownBy(() -> cellService.revealCell(gameBean, cellId))
			.isInstanceOf(MineExplodedException.class)
			.hasMessage(String.format("Mine found in cellId=%s gameId=%s", cellId, gameBean.getId()));

		verify(cell).setCellStatus(CellStatus.REVEALED);
	}
}
