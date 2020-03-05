package com.stvmallen.minesweeper.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.stvmallen.minesweeper.entity.Cell;
import com.stvmallen.minesweeper.entity.Game;
import com.stvmallen.minesweeper.exception.GameNotFoundException;
import com.stvmallen.minesweeper.model.GameBean;
import com.stvmallen.minesweeper.model.NewGameRequest;
import com.stvmallen.minesweeper.repository.GameRepository;
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
		return null;
	}

	@Override
	public GameBean pauseGame(Long gameId) {
		return null;
	}

	@Override
	public GameBean resumeGame(Long gameId) {
		return null;
	}

	private Game createNewGame(NewGameRequest request, List<Cell> newGameCells) {
		return new Game(request.getColumnCount(), request.getRowCount(), request.getMineCount(), GameStatus.NEW, newGameCells);
	}
}
