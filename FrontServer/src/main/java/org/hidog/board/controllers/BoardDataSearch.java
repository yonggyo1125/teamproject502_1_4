package org.hidog.board.controllers;

import lombok.Data;
import org.hidog.global.CommonSearch;

import java.util.List;

@Data
public class BoardDataSearch extends CommonSearch { // 검색을 위한 커맨드 객체

    private int limit;

    private String bid; // 게시판 ID
    private List<String> bids; // 게시판 ID 여러개

    private String sort; // 정렬 조건
}
