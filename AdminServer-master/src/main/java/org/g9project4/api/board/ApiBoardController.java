package org.g9project4.api.board;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.Board;
import org.g9project4.board.services.BoardConfigInfoService;
import org.g9project4.global.exceptions.RestExceptionProcessor;
import org.g9project4.global.rests.JSONData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor

public class ApiBoardController implements RestExceptionProcessor {
    private final BoardConfigInfoService infoService;

    @GetMapping("/config/{bid}")
    public JSONData getBoard(@PathVariable("bid") String bid){
        Board board = infoService.get(bid);
        return new JSONData(board);
    }

}
