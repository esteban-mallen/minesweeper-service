package com.stvmallen.minesweeper.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.stvmallen.minesweeper.types.GameStatus;

@Data
@NoArgsConstructor
public class GameBean implements Serializable {
	private static final long serialVersionUID = -5132839680721958365L;

	private Long id;
	private Instant createdOn;
	private Instant lastModified;
	private Long columnCount;
	private Long rowCount;
	private Long mineCount;
	private GameStatus status;
	private List<CellBean> cells;
}
