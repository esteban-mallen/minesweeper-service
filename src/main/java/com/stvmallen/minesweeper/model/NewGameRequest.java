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

	@NotNull
	@Min(1)
	private Long rowCount;
	@NotNull
	@Min(1)
	private Long columnCount;
	@NotNull
	private Long mineCount;
}
