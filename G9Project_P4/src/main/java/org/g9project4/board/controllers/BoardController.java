package org.g9project4.board.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.Board;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.services.BoardConfigInfoService;
import org.g9project4.board.services.BoardDeleteService;
import org.g9project4.board.services.BoardInfoService;
import org.g9project4.board.services.BoardSaveService;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController implements ExceptionProcessor {
    private final BoardConfigInfoService configInfoService;
    private final BoardInfoService infoService;
    private final BoardSaveService saveService;
    private final BoardDeleteService deleteService;
    private final Utils utils;


    private Board board; // 게시판 설정
    private BoardData boardData; // 게시글 내용

    /**
     * 글 쓰기
     * @param bid
     * @return
     */
    @GetMapping("/write/{bid}")
    public String write(@PathVariable("bid") String bid, Model model) {
        commonProcess(bid, "write", model);

        return utils.tpl("board/write");
    }

    // 글 수정
    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq) {

        return utils.tpl("board/update");
    }

    // 글 작성, 수정 처리
    @PostMapping("/save")
    public String save(RequestBoard form, Model model) {
        commonProcess(form.getBid(), form.getMode(), model);

        return null;
    }

    @GetMapping("/list/{bid}")
    public String list(@PathVariable("bid") String bid, @ModelAttribute BoardDataSearch search, Model model) {
        commonProcess(bid, "list", model);

        return utils.tpl("board/list");
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq) {

        return utils.tpl("board/view");
    }

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq) {

        return null;
    }


    /**
     * 게시판 설정이 필요한 공통 처리(모든 처리)
     *
     * @param bid : 게시판 아이디
     * @param mode
     * @param model
     */
    private void commonProcess(String bid, String mode, Model model) {

    }
}
