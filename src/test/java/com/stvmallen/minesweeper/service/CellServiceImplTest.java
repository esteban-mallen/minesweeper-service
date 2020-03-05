package com.stvmallen.minesweeper.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.api.Condition;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stvmallen.minesweeper.model.CellBean;
import com.stvmallen.minesweeper.model.NewGameRequest;

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
}
