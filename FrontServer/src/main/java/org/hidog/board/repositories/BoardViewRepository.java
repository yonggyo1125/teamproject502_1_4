package org.hidog.board.repositories;

import org.hidog.board.entities.BoardView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface BoardViewRepository extends JpaRepository<BoardView, Long>, QuerydslPredicateExecutor<BoardView> {
}
