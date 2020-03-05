package com.stvmallen.minesweeper.model;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewGameRequest implements Serializable {
	private static final long serialVersionUID = -5829690699166513526L;

	@NotNull(message = "Row count can't be null")
	@Min(value = 1, message = "Row count must be greater than 0")
	private Long rowCount;
	@NotNull(message = "Column count can't be null")
	@Min(value = 1, message = "Column count must be greater than 0")
	private Long columnCount;
	@NotNull(message = "Mine count can't be null")
	private Long mineCount;
}
