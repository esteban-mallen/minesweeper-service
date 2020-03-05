package com.stvmallen.minesweeper.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.stvmallen.minesweeper.types.GameStatus;

@Entity
@Table(name = "games")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Game extends TimestampedEntity {
	@Column(name = "column_count", nullable = false)
	private Long columnCount;

	@Column(name = "row_count", nullable = false)
	private Long rowCount;

	@Column(name = "mine_count", nullable = false)
	private Long mineCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 10, nullable = false)
	private GameStatus status;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "game_id")
	private List<Cell> cells;
}
