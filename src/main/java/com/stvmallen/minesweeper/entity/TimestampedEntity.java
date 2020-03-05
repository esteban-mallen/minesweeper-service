package com.stvmallen.minesweeper.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
abstract class TimestampedEntity extends AbstractEntity {
	@Column(name = "created_on", nullable = false)
	private Instant createdOn;
	@Column(name = "last_modified", nullable = false)
	private Instant lastModified;
}
