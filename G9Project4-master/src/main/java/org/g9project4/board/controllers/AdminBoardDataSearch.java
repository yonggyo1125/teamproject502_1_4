package org.g9project4.board.controllers;

import lombok.Data;

import java.util.List;

@Data
public class AdminBoardDataSearch extends BoardDataSearch {
    private List<Long> memberSeq;
}