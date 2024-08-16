package org.g9project4.board.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.services.BoardConfigInfoService;
import org.g9project4.board.services.BoardDeleteService;
import org.g9project4.board.services.BoardInfoService;
import org.g9project4.board.services.BoardSaveService;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController implements ExceptionProcessor {
    private final BoardConfigInfoService configInfoService;
    private final BoardInfoService infoService;
    private final BoardSaveService saveService;
    private final BoardDeleteService deleteService;
    private final Utils utils;

    /**
     * 글 쓰기
     * @param bid
     * @return
     */
    @GetMapping("/write/{bid}")
    public String write(@PathVariable("bid") String bid) {

        return utils.tpl("board/write");
    }

    // 글 수정
    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq) {

        return utils.tpl("board/update");
    }

    // 글 작성, 수정 처리
    @PostMapping("/save")
    public String save() {

        return null;
    }

    @GetMapping("/list/{bid}")
    public String list(@PathVariable("bid") String bid) {

        return utils.tpl("board/list");
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq) {

        return utils.tpl("board/view");
    }
}
