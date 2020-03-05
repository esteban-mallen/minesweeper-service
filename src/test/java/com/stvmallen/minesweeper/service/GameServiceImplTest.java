package com.stvmallen.minesweeper.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.stvmallen.minesweeper.entity.Cell;
import com.stvmallen.minesweeper.entity.Game;
import com.stvmallen.minesweeper.exception.GameNotFoundException;
import com.stvmallen.minesweeper.model.CellBean;
import com.stvmallen.minesweeper.model.GameBean;
import com.stvmallen.minesweeper.model.NewGameRequest;
import com.stvmallen.minesweeper.repository.GameRepository;
import ma.glasnost.orika.MapperFacade;

public class GameServiceImplTest {
	@Mock
	private MapperFacade mapper;
	@Mock
	private GameRepository gameRepository;
	@Mock
	private CellService cellService;
	@Captor
	private ArgumentCaptor<Game> gameArgumentCaptor;

	private GameServiceImpl gameService;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		gameService = new GameServiceImpl(mapper, gameRepository, cellService);
	}

	@Test
	public void testCreateNewGame() {
		NewGameRequest request = new NewGameRequest(10L, 11L, 3L);
		CellBean cellBean = mock(CellBean.class);
		Cell cell = mock(Cell.class);
		Game savedGame = mock(Game.class);
		GameBean savedGameBean = mock(GameBean.class);

		when(cellService.createCells(request)).thenReturn(ImmutableList.of(cellBean));
		when(mapper.map(cellBean, Cell.class)).thenReturn(cell);
		when(gameRepository.save(gameArgumentCaptor.capture())).thenReturn(savedGame);
		when(mapper.map(savedGame, GameBean.class)).thenReturn(savedGameBean);

		GameBean result = gameService.createNewGame(request);

		assertThat(result).isSameAs(savedGameBean);
		assertThat(gameArgumentCaptor.getValue()).satisfies(game -> {
			assertThat(game.getCells()).containsExactly(cell);
			assertThat(game.getColumnCount()).isEqualTo(11L);
			assertThat(game.getRowCount()).isEqualTo(10L);
			assertThat(game.getMineCount()).isEqualTo(3L);
		});
	}

	@Test
	public void testFindGame_noGameFound() {
		when(gameRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> gameService.findGame(1L))
			.isInstanceOf(GameNotFoundException.class)
			.hasMessage("Game not found with gameId=1");
	}

	@Test
	public void testFindGame() {
		Game game = mock(Game.class);
		GameBean gameBean = mock(GameBean.class);

		when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
		when(mapper.map(game, GameBean.class)).thenReturn(gameBean);

		GameBean foundGame = gameService.findGame(1L);

		assertThat(foundGame).isSameAs(gameBean);
	}
}
