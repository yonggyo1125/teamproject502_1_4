package org.hidog.board.controllers;

import lombok.Data;
import org.hidog.global.CommonSearch;

import java.util.List;

@Data
public class BoardSearch extends CommonSearch{
    /**
     * sopt 검색옵션
     * ALL - (통합검색) - bid, bName
     * bid - 게시판아이디 검색
     * bName - 게시판이름 검색
     */
    private String bid; //게시판 id
    private List<String> bids;

    private String bName; //게시판 이름
    private boolean active; //사용 미사용

}
