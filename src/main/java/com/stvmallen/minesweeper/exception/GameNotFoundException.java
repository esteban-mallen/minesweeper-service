package com.stvmallen.minesweeper.exception;

public class GameNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 620282269086896604L;

	public GameNotFoundException(String message) {
		super(message);
	}
}
