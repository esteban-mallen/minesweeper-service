package com.stvmallen.minesweeper.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.stvmallen.minesweeper.types.CellStatus;

@Entity
@Table(name = "cells")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Cell extends TimestampedEntity {
	@Column(name = "row_position", nullable = false)
	private Long rowPosition;

	@Column(name = "column_position", nullable = false)
	private Long columnPosition;

	@Column(name = "is_mine", nullable = false)
	private boolean isMine;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 10, nullable = false)
	private CellStatus cellStatus = CellStatus.HIDDEN;

	@Column(name = "adjacent_mine_count", nullable = false)
	private Long adjacentMineCount;
}
