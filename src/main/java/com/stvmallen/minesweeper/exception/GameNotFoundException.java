package com.stvmallen.minesweeper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 620282269086896604L;

	public GameNotFoundException(String message) {
		super(message);
	}
}
