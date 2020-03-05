package com.stvmallen.minesweeper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stvmallen.minesweeper.entity.Cell;

@Repository
public interface CellRepository extends CrudRepository<Cell, Long> {

}
