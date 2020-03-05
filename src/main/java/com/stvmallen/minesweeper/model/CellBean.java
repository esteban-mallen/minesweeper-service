package com.stvmallen.minesweeper.model;

import java.io.Serializable;
import java.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.stvmallen.minesweeper.types.CellStatus;

@Data
@NoArgsConstructor
public class CellBean implements Serializable {
	private static final long serialVersionUID = 399709298295536090L;

	private Long id;
	private Instant createdOn;
	private Instant lastModified;
	private Long rowPosition;
	private Long columnPosition;
	private boolean isMine;
	private CellStatus cellStatus;
}
