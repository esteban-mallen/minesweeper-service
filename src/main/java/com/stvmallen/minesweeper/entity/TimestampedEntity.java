package com.stvmallen.minesweeper.entity;

import java.time.Instant;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TimestampedEntity extends AbstractEntity {
	private Instant createdOn;
	private Instant lastModified;
}
