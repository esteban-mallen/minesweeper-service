package com.stvmallen.minesweeper.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TimestampedEntity extends AbstractEntity {
	@Column(name = "created_on", nullable = false)
	private Instant createdOn;
	@Column(name = "last_modified", nullable = false)
	private Instant lastModified;
}
