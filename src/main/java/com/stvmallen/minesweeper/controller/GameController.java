package com.stvmallen.minesweeper.controller;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stvmallen.minesweeper.model.GameBean;
import com.stvmallen.minesweeper.model.NewGameRequest;
import com.stvmallen.minesweeper.service.GameService;

@RestController
@RequestMapping(value = "/api/v1/minesweeper/games", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class GameController {
	private final GameService gameService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameBean> createGame(@Valid @RequestBody NewGameRequest request) {
		log.info("Received new game request, request={}", request);
		return ResponseEntity.ok(gameService.createNewGame(request));
	}

	@GetMapping(value = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameBean> findGame(@PathVariable Long gameId) {
		log.info("Finding gameId={}", gameId);
		return ResponseEntity.ok(gameService.findGame(gameId));
	}

	@PostMapping(value = "/{gameId}/cells/{cellId}/reveal", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameBean> revealCell(@PathVariable Long gameId, @PathVariable Long cellId) {
		log.info("Revealing cellId={} in gameId={}", cellId, gameId);
		return ResponseEntity.ok(gameService.revealCell(gameId, cellId));
	}

	@PostMapping(value = "/{gameId}/cells/{cellId}/flag", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameBean> flagCell(@PathVariable Long gameId, @PathVariable Long cellId) {
		log.info("Flagging cellId={} in gameId={}", cellId, gameId);
		return ResponseEntity.ok(gameService.flagCell(gameId, cellId));
	}

	@PostMapping(value = "/{gameId}/cells/{cellId}/mark", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameBean> markCell(@PathVariable Long gameId, @PathVariable Long cellId) {
		log.info("Marking cellId={} in gameId={}", cellId, gameId);
		return ResponseEntity.ok(gameService.markCell(gameId, cellId));
	}

	@PatchMapping(value = "/{gameId}/pause", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameBean> pauseGame(@PathVariable Long gameId) {
		log.info("Pausing gameId={}", gameId);
		return ResponseEntity.ok(gameService.pauseGame(gameId));
	}

	@PatchMapping(value = "/{gameId}/resume", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameBean> resumeGame(@PathVariable Long gameId) {
		log.info("Resuming gameId={}", gameId);
		return ResponseEntity.ok(gameService.resumeGame(gameId));
	}
}
