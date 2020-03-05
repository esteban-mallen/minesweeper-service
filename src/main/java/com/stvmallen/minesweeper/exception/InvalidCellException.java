package com.stvmallen.minesweeper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidCellException extends RuntimeException {
	private static final long serialVersionUID = -5335434223043516438L;

	public InvalidCellException(String message) {
		super(message);
	}
}
