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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/v1/minesweeper/games", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class GameController {
	private final GameService gameService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
		value = "Creates a new game with the given parameters",
		httpMethod = "POST",
		produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Game created", response = GameBean.class),
		@ApiResponse(code = 400, message = "Invalid request"),
		@ApiResponse(code = 500, message = "Unexpected error")})
	public ResponseEntity<GameBean> createGame(@Valid @RequestBody NewGameRequest request) {
		log.info("Received new game request, request={}", request);
		return ResponseEntity.ok(gameService.createNewGame(request));
	}

	@GetMapping(value = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
		value = "Finds an existing game",
		httpMethod = "GET",
		produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Game found", response = GameBean.class),
		@ApiResponse(code = 404, message = "Game not found"),
		@ApiResponse(code = 500, message = "Unexpected error")})
	public ResponseEntity<GameBean> findGame(@PathVariable Long gameId) {
		log.info("Finding gameId={}", gameId);
		return ResponseEntity.ok(gameService.findGame(gameId));
	}

	@PostMapping(value = "/{gameId}/cells/{cellId}/reveal", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
		value = "Reveals the cell passed as parameter in the given game",
		httpMethod = "POST",
		produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Cell revealed", response = GameBean.class),
		@ApiResponse(code = 404, message = "Game not found"),
		@ApiResponse(code = 400, message = "Invalid cell id provided"),
		@ApiResponse(code = 500, message = "Unexpected error")})
	public ResponseEntity<GameBean> revealCell(@PathVariable Long gameId, @PathVariable Long cellId) {
		log.info("Revealing cellId={} in gameId={}", cellId, gameId);
		return ResponseEntity.ok(gameService.revealCell(gameId, cellId));
	}

	@PostMapping(value = "/{gameId}/cells/{cellId}/flag", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
		value = "Flags the cell passed as parameter in the given game",
		httpMethod = "POST",
		produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Cell flagged", response = GameBean.class),
		@ApiResponse(code = 404, message = "Game not found"),
		@ApiResponse(code = 400, message = "Invalid cell id provided"),
		@ApiResponse(code = 500, message = "Unexpected error")})
	public ResponseEntity<GameBean> flagCell(@PathVariable Long gameId, @PathVariable Long cellId) {
		log.info("Flagging cellId={} in gameId={}", cellId, gameId);
		return ResponseEntity.ok(gameService.flagCell(gameId, cellId));
	}

	@PostMapping(value = "/{gameId}/cells/{cellId}/mark", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
		value = "Marks the cell passed as parameter in the given game",
		httpMethod = "POST",
		produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Cell marked", response = GameBean.class),
		@ApiResponse(code = 404, message = "Game not found"),
		@ApiResponse(code = 400, message = "Invalid cell id provided"),
		@ApiResponse(code = 500, message = "Unexpected error")})
	public ResponseEntity<GameBean> markCell(@PathVariable Long gameId, @PathVariable Long cellId) {
		log.info("Marking cellId={} in gameId={}", cellId, gameId);
		return ResponseEntity.ok(gameService.markCell(gameId, cellId));
	}

	@PatchMapping(value = "/{gameId}/pause", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
		value = "Pauses the given game",
		httpMethod = "PATCH",
		produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Game paused", response = GameBean.class),
		@ApiResponse(code = 404, message = "Game not found"),
		@ApiResponse(code = 500, message = "Unexpected error")})
	public ResponseEntity<GameBean> pauseGame(@PathVariable Long gameId) {
		log.info("Pausing gameId={}", gameId);
		return ResponseEntity.ok(gameService.pauseGame(gameId));
	}

	@PatchMapping(value = "/{gameId}/resume", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
		value = "Resumes the given paused game",
		httpMethod = "PATCH",
		produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Game resumed", response = GameBean.class),
		@ApiResponse(code = 404, message = "Game not found"),
		@ApiResponse(code = 500, message = "Unexpected error")})
	public ResponseEntity<GameBean> resumeGame(@PathVariable Long gameId) {
		log.info("Resuming gameId={}", gameId);
		return ResponseEntity.ok(gameService.resumeGame(gameId));
	}
}
