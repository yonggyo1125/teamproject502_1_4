package org.g9project4.board.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.Board;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.exceptions.BoardNotFoundException;
import org.g9project4.board.services.BoardConfigInfoService;
import org.g9project4.board.services.BoardDeleteService;
import org.g9project4.board.services.BoardInfoService;
import org.g9project4.board.services.BoardSaveService;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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


        // 목록 또는 상세 보기 이동
        String url = board.getLocationAfterWriting().equals("list") ? "/board/list/" + board.getBid() : "/board/view/" + boardData.getSeq();

        return utils.redirectUrl(url);
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

    // 게시글 삭제
    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "delete", model);


        return utils.redirectUrl("/board/list/" + board.getBid());
    }


    /**
     * 게시판 설정이 필요한 공통 처리(모든 처리)
     *
     * @param bid : 게시판 아이디
     * @param mode
     * @param model
     */
    private void commonProcess(String bid, String mode, Model model) {
        board = configInfoService.get(bid).orElseThrow(BoardNotFoundException::new); // 게시판 설정

        List<String> addCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        mode = mode == null || !StringUtils.hasText(mode.trim()) ? "write" : mode.trim();

        String skin = board.getSkin(); // 스킨

        // 게시판 공통 CSS
        addCss.add("board/style");

        // 스킨별 공통 CSS
        addCss.add("board/" + skin + "/style");

        if (mode.equals("write") || mode.equals("update")) {
            // 글쓰기, 수정
            // 파일 업로드, 에디터 - 공통
            // form.js
            // 파일 첨부, 에디터 이미지 첨부를 사용하는 경우
            if (board.isUseUploadFile() || board.isUseUploadImage()) {
                addCommonScript.add("fileManager");
            }

            // 에디터 사용의 경우
            if (board.isUseEditor()) {
                addCommonScript.add("ckeditor5/ckeditor");
            }

            addScript.add("board/" + skin + "/form");
        }

        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("board", board); // 게시판 설정
    }

    /**
     * 게시글 번호가 경로 변수로 들어오는 공통 처리
     *  게시판 설정 + 게시글 내용
     *
     * @param seq
     * @param mode
     * @param model
     */
    private void commonProcess(Long seq, String mode, Model model) {
        boardData = infoService.get(seq);

        model.addAttribute("boardData", boardData);

        commonProcess(boardData.getBoard().getBid(), mode, model);
    }
}
