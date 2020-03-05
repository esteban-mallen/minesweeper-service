package com.stvmallen.minesweeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stvmallen.minesweeper.entity.Cell;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {
}
