package org.hidog.board.repositories;

import org.hidog.board.entities.BoardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface SaveBoardDataRepository extends JpaRepository<BoardData, Integer>, QuerydslPredicateExecutor<BoardData> {
}
