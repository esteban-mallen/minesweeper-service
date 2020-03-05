package com.stvmallen.minesweeper.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {
	private final MapperFacade mapper;
	private final GameRepository gameRepository;
	private final CellService cellService;

	@Override
	public GameBean createNewGame(NewGameRequest request) {
		log.info("Creating new game for request={}", request);

		List<Cell> newGameCells = cellService.createCells(request)
			.stream()
			.map(cellBean -> mapper.map(cellBean, Cell.class))
			.collect(Collectors.toList());

		Game newGame = gameRepository.save(createNewGame(request, newGameCells));

		log.info("Saved new game with id={}", newGame.getId());

		return mapper.map(newGame, GameBean.class);
	}

	@Override
	public GameBean findGame(Long gameId) {
		return gameRepository.findById(gameId)
			.map(game -> mapper.map(game, GameBean.class))
			.orElseThrow(() -> new GameNotFoundException("Game not found with gameId=" + gameId));
	}

	@Override
	public GameBean revealCell(Long gameId, Long cellId) {
		GameBean gameBean = findActiveGame(gameId);
		gameBean.setStatus(GameStatus.STARTED);

		try {
			cellService.revealCell(gameBean, cellId);

			checkGameStatus(gameBean);
		} catch (MineExplodedException e) {
			log.info("Revealed mine in cellId={} gameId={}. Marking game as finished.", cellId, gameId);
			gameBean.setStatus(GameStatus.FINISHED);
		}

		return mapper.map(gameRepository.save(mapper.map(gameBean, Game.class)), GameBean.class);
	}

	@Override
	public GameBean pauseGame(Long gameId) {
		log.info("Pausing gameId={}", gameId);
		return changeGameStatus(gameId, GameStatus.PAUSED);
	}

	@Override
	public GameBean resumeGame(Long gameId) {
		log.info("Resuming gameId={}", gameId);
		return changeGameStatus(gameId, GameStatus.STARTED);
	}

	@Override
	public GameBean markCell(Long gameId, Long cellId) {
		GameBean gameBean = findActiveGame(gameId);
		gameBean.setStatus(GameStatus.STARTED);

		cellService.markCell(gameBean, cellId);

		return mapper.map(gameRepository.save(mapper.map(gameBean, Game.class)), GameBean.class);
	}

	@Override
	public GameBean flagCell(Long gameId, Long cellId) {
		GameBean gameBean = findActiveGame(gameId);
		gameBean.setStatus(GameStatus.STARTED);

		cellService.flagCell(gameBean, cellId);
		checkGameStatus(gameBean);

		return mapper.map(gameRepository.save(mapper.map(gameBean, Game.class)), GameBean.class);
	}

	private Game createNewGame(NewGameRequest request, List<Cell> newGameCells) {
		return new Game(request.getColumnCount(), request.getRowCount(), request.getMineCount(), GameStatus.NEW, newGameCells);
	}

	private GameBean changeGameStatus(Long gameId, GameStatus status) {
		return gameRepository.findById(gameId)
			.filter(game -> game.getStatus() != GameStatus.FINISHED)
			.map(game -> {
				game.setStatus(status);
				return game;
			})
			.map(gameRepository::save)
			.map(game -> mapper.map(game, GameBean.class))
			.orElseThrow(() -> new GameNotFoundException("Active game not found with gameId=" + gameId));
	}

	private void checkGameStatus(GameBean gameBean) {
		if (gameBean.getCells().stream()
			.filter(cellBean -> CellStatus.HIDDEN == cellBean.getCellStatus() || CellStatus.FLAGGED == cellBean.getCellStatus())
			.allMatch(CellBean::isMine)) {
			gameBean.setStatus(GameStatus.FINISHED); //Game won
		}
	}

	private GameBean findActiveGame(Long gameId) {
		return gameRepository.findByIdAndStatusIn(gameId, GameStatus.STARTED, GameStatus.NEW)
			.map(game -> mapper.map(game, GameBean.class))
			.orElseThrow(() -> new GameNotFoundException("Active game not found with gameId=" + gameId));
	}
}
