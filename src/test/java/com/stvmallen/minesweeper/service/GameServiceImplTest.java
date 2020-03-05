package com.stvmallen.minesweeper.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
import com.stvmallen.minesweeper.exception.MineExplodedException;
import com.stvmallen.minesweeper.model.CellBean;
import com.stvmallen.minesweeper.model.GameBean;
import com.stvmallen.minesweeper.model.NewGameRequest;
import com.stvmallen.minesweeper.repository.GameRepository;
import com.stvmallen.minesweeper.types.CellStatus;
import com.stvmallen.minesweeper.types.GameStatus;
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

	@Test
	public void testPauseGame() {
		Game game = mock(Game.class);
		GameBean gameBean = mock(GameBean.class);

		when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
		when(game.getStatus()).thenReturn(GameStatus.STARTED);
		when(gameRepository.save(gameArgumentCaptor.capture())).thenReturn(game);
		when(mapper.map(game, GameBean.class)).thenReturn(gameBean);

		gameService.pauseGame(1L);

		verify(game).setStatus(GameStatus.PAUSED);
		verify(gameRepository).save(game);
	}

	@Test
	public void testPauseGame_gameNotFound() {
		Game game = mock(Game.class);

		when(gameRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> gameService.pauseGame(1L))
			.isInstanceOf(GameNotFoundException.class)
			.hasMessage("Active game not found with gameId=1");

		verify(gameRepository, never()).save(game);
	}

	@Test
	public void testPauseGame_gameFinished() {
		Game game = mock(Game.class);

		when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
		when(game.getStatus()).thenReturn(GameStatus.FINISHED);
		when(gameRepository.save(gameArgumentCaptor.capture())).thenReturn(game);

		assertThatThrownBy(() -> gameService.pauseGame(1L))
			.isInstanceOf(GameNotFoundException.class)
			.hasMessage("Active game not found with gameId=1");

		verify(gameRepository, never()).save(game);
	}

	@Test
	public void testResumeGame() {
		Game game = mock(Game.class);
		GameBean gameBean = mock(GameBean.class);

		when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
		when(game.getStatus()).thenReturn(GameStatus.PAUSED);
		when(gameRepository.save(gameArgumentCaptor.capture())).thenReturn(game);
		when(mapper.map(game, GameBean.class)).thenReturn(gameBean);

		gameService.resumeGame(1L);

		verify(game).setStatus(GameStatus.STARTED);
		verify(gameRepository).save(game);
	}

	@Test
	public void testResumeGame_gameNotFound() {
		Game game = mock(Game.class);

		when(gameRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> gameService.resumeGame(1L))
			.isInstanceOf(GameNotFoundException.class)
			.hasMessage("Active game not found with gameId=1");

		verify(gameRepository, never()).save(game);
	}

	@Test
	public void testResumeGame_gameFinished() {
		Game game = mock(Game.class);

		when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
		when(game.getStatus()).thenReturn(GameStatus.FINISHED);
		when(gameRepository.save(gameArgumentCaptor.capture())).thenReturn(game);

		assertThatThrownBy(() -> gameService.resumeGame(1L))
			.isInstanceOf(GameNotFoundException.class)
			.hasMessage("Active game not found with gameId=1");

		verify(gameRepository, never()).save(game);
	}

	@Test
	public void testMarkCell() {
		Game game = mock(Game.class);
		GameBean gameBean = mock(GameBean.class);

		when(gameRepository.findByIdAndStatusIn(1L, GameStatus.STARTED, GameStatus.NEW)).thenReturn(Optional.of(game));
		when(gameRepository.save(game)).thenReturn(game);
		when(mapper.map(game, GameBean.class)).thenReturn(gameBean);
		when(mapper.map(gameBean, Game.class)).thenReturn(game);

		GameBean returnedGame = gameService.markCell(1L, 1L);

		assertThat(returnedGame).isSameAs(gameBean);

		verify(gameBean).setStatus(GameStatus.STARTED);
		verify(cellService).markCell(gameBean, 1L);
	}

	@Test
	public void testMarkCell_noGameFound() {
		Game game = mock(Game.class);

		when(gameRepository.findByIdAndStatusIn(1L, GameStatus.STARTED, GameStatus.NEW)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> gameService.markCell(1L, 1L))
			.isInstanceOf(GameNotFoundException.class)
			.hasMessage("Active game not found with gameId=1");

		verify(gameRepository, never()).save(game);
	}

	@Test
	public void testFlagCell() {
		Game game = mock(Game.class);
		GameBean gameBean = mock(GameBean.class);
		CellBean cellBean = mock(CellBean.class);

		when(gameRepository.findByIdAndStatusIn(1L, GameStatus.STARTED, GameStatus.NEW)).thenReturn(Optional.of(game));
		when(gameRepository.save(game)).thenReturn(game);
		when(mapper.map(game, GameBean.class)).thenReturn(gameBean);
		when(mapper.map(gameBean, Game.class)).thenReturn(game);
		when(gameBean.getCells()).thenReturn(List.of(cellBean));
		when(cellBean.getCellStatus()).thenReturn(CellStatus.FLAGGED);
		when(cellBean.isMine()).thenReturn(false);

		GameBean returnedGame = gameService.flagCell(1L, 1L);

		assertThat(returnedGame).isSameAs(gameBean);

		verify(gameBean).setStatus(GameStatus.STARTED);
		verify(gameBean, never()).setStatus(GameStatus.FINISHED);
		verify(cellService).flagCell(gameBean, 1L);
	}

	@Test
	public void testFlagCell_gameWon() {
		Game game = mock(Game.class);
		GameBean gameBean = mock(GameBean.class);
		CellBean cellBean = mock(CellBean.class);

		when(gameRepository.findByIdAndStatusIn(1L, GameStatus.STARTED, GameStatus.NEW)).thenReturn(Optional.of(game));
		when(gameRepository.save(game)).thenReturn(game);
		when(mapper.map(game, GameBean.class)).thenReturn(gameBean);
		when(mapper.map(gameBean, Game.class)).thenReturn(game);
		when(gameBean.getCells()).thenReturn(List.of(cellBean));
		when(cellBean.getCellStatus()).thenReturn(CellStatus.FLAGGED);
		when(cellBean.isMine()).thenReturn(true);

		GameBean returnedGame = gameService.flagCell(1L, 1L);

		assertThat(returnedGame).isSameAs(gameBean);

		verify(gameBean).setStatus(GameStatus.STARTED);
		verify(gameBean).setStatus(GameStatus.FINISHED);
		verify(cellService).flagCell(gameBean, 1L);
	}

	@Test
	public void testRevealCell() throws MineExplodedException {
		Game game = mock(Game.class);
		GameBean gameBean = mock(GameBean.class);
		CellBean cellBean = mock(CellBean.class);

		when(gameRepository.findByIdAndStatusIn(1L, GameStatus.STARTED, GameStatus.NEW)).thenReturn(Optional.of(game));
		when(gameRepository.save(game)).thenReturn(game);
		when(mapper.map(game, GameBean.class)).thenReturn(gameBean);
		when(mapper.map(gameBean, Game.class)).thenReturn(game);
		when(gameBean.getCells()).thenReturn(List.of(cellBean));

		gameService.revealCell(1L, 1L);

		verify(cellService).revealCell(gameBean, 1L);
		verify(gameRepository).save(game);
	}

	@Test
	public void testRevealCell_gameWon() throws MineExplodedException {
		Game game = mock(Game.class);
		GameBean gameBean = mock(GameBean.class);
		CellBean cellBean = mock(CellBean.class);

		when(gameRepository.findByIdAndStatusIn(1L, GameStatus.STARTED, GameStatus.NEW)).thenReturn(Optional.of(game));
		when(gameRepository.save(game)).thenReturn(game);
		when(mapper.map(game, GameBean.class)).thenReturn(gameBean);
		when(mapper.map(gameBean, Game.class)).thenReturn(game);
		when(gameBean.getCells()).thenReturn(List.of(cellBean));
		when(cellBean.getCellStatus()).thenReturn(CellStatus.FLAGGED);
		when(cellBean.isMine()).thenReturn(true);

		gameService.revealCell(1L, 1L);

		verify(cellService).revealCell(gameBean, 1L);
		verify(gameBean).setStatus(GameStatus.FINISHED);
		verify(gameRepository).save(game);
	}

	@Test
	public void testRevealCell_mineExploded() throws MineExplodedException {
		Game game = mock(Game.class);
		GameBean gameBean = mock(GameBean.class);
		CellBean cellBean = mock(CellBean.class);

		when(gameRepository.findByIdAndStatusIn(1L, GameStatus.STARTED, GameStatus.NEW)).thenReturn(Optional.of(game));
		when(gameRepository.save(game)).thenReturn(game);
		when(mapper.map(game, GameBean.class)).thenReturn(gameBean);
		when(mapper.map(gameBean, Game.class)).thenReturn(game);
		when(gameBean.getCells()).thenReturn(List.of(cellBean));
		doThrow(new MineExplodedException("Mine exploded!!")).when(cellService).revealCell(gameBean, 1L);

		gameService.revealCell(1L, 1L);

		verify(gameBean).setStatus(GameStatus.FINISHED);
		verify(gameRepository).save(game);
	}
}
