package org.hidog.mypage.repositories;

import org.hidog.board.entities.BoardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyPostRepository extends JpaRepository<BoardData, Long> {
    List<BoardData> findByMemberUserName(String userName);
}