package org.g9project4.board.controllers;

import lombok.Data;
import org.g9project4.board.entities.BoardData;

import java.util.List;

@Data
public class RequestAdminList {
    private List<BoardData> items;

}
