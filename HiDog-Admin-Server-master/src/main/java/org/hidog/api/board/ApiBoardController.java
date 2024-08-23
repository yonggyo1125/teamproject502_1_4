package org.hidog.api.board;

import lombok.RequiredArgsConstructor;
import org.hidog.board.entities.Board;
import org.hidog.board.services.BoardConfigInfoService;
import org.hidog.global.exceptions.RestExceptionProcessor;
import org.hidog.global.rests.JSONData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class ApiBoardController implements RestExceptionProcessor {

    private final BoardConfigInfoService infoService;

    /**
     * 게시판 설정
     *
     * @param bid
     * @return
     */

    @GetMapping("/config/{bid}")
    public JSONData getBoard(@PathVariable("bid") String bid) {
        Board board = infoService.get(bid);

        return new JSONData(board);
    }

    /**
     * 게시판 리스트
     *
     * @return
     */
    @GetMapping
    public JSONData getBoardList() {
        List<String[]> data = infoService.getBoardList();

        return new JSONData(data);
    }

}
