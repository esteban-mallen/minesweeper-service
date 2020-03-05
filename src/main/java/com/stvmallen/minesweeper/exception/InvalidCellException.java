package com.stvmallen.minesweeper.exception;

public class InvalidCellException extends RuntimeException {
	private static final long serialVersionUID = -5335434223043516438L;

	public InvalidCellException(String message) {
		super(message);
	}
}
