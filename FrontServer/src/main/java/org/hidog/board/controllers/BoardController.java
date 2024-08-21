package org.hidog.board.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hidog.board.entities.Board;
import org.hidog.board.entities.BoardData;
import org.hidog.board.exceptions.BoardNotFoundException;
import org.hidog.board.services.BoardConfigInfoService;
import org.hidog.board.services.BoardDeleteService;
import org.hidog.board.services.BoardInfoService;
import org.hidog.board.services.BoardSaveService;
import org.hidog.board.validators.BoardValidator;
import org.hidog.file.constants.FileStatus;
import org.hidog.file.entities.FileInfo;
import org.hidog.file.services.FileInfoService;
import org.hidog.global.ListData;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.hidog.global.services.ApiConfigService;
import org.hidog.member.MemberUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@SessionAttributes({"boardData"}) // 수정이후에는 세션 비우기?
public class BoardController implements ExceptionProcessor {

    private final BoardConfigInfoService configInfoService;
    private final BoardInfoService boardInfoService;
    private final BoardDeleteService boardDeleteService;
    private final BoardSaveService boardSaveService;
    private final BoardValidator boardValidator;
    private final Utils utils;
    private final ApiConfigService apiConfigService;

    private final MemberUtil memberUtil;
    private final FileInfoService fileInfoService;



    private Board board; // 게시판 설정
    private BoardData boardData; // 게시글

    // 티맵 api 키 조회
    @ModelAttribute("tmapJavascriptKey")
    public String tmapJavascriptKey() {
        return apiConfigService.get("tmapJavascriptKey");
    }


    /**
     * 게시글 작성
     *
     * @param bid : 게시판 아이디
     * @param model
     * @return
     */
    @GetMapping("/write/{bid}")
    public String write(@PathVariable("bid") String bid,
                        @ModelAttribute RequestBoard form, Model model) {
        commonProcess(bid, "write", model);

        form.setGuest(!memberUtil.isLogin());
        if (memberUtil.isLogin()) {
            form.setPoster(memberUtil.getMember().getUserName());
        }



        return utils.tpl("board/write");
    }

    /**
     * 게시글 수정
     *
     * @param seq : 게시글 번호
     * @param model
     * @return
     */
    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "update", model);

        RequestBoard form = boardInfoService.getForm(boardData); // 쿼리를 2번하지 않고 바로 쓰기 위해서 seq말고 boardData 사용함
        model.addAttribute("requestBoard", form);


        return utils.tpl("board/update");
    }

    /**
     * 게시글 등록, 수정
     *
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestBoard form, Errors errors, Model model, SessionStatus status, HttpSession session) {
        String mode = form.getMode();
        mode = mode != null && StringUtils.hasText(mode.trim()) ? mode.trim() : "write";
        commonProcess(form.getBid(), mode, model);

        boolean isGuest = (mode.equals("write") && !memberUtil.isLogin());
        if(mode.equals("update")) {
            BoardData data = (BoardData)model.getAttribute("boardData");
            isGuest = data.getMember() == null;
        }


        form.setGuest(isGuest);

        boardValidator.validate(form, errors);

        if (errors.hasErrors()) {
            // 업로드 된 파일 목록 - location : editor, attach
            String gid = form.getGid();
            List<FileInfo> editorImages = fileInfoService.getList(gid, "editor", FileStatus.ALL);
            List<FileInfo> attachFiles = fileInfoService.getList(gid, "attach", FileStatus.ALL);
            form.setEditorImages(editorImages);
            form.setAttachFiles(attachFiles);

            return utils.tpl("board/" + mode);
        }

        // 게시글 저장 처리
        BoardData boardData = boardSaveService.save(form);

        status.setComplete();
        session.removeAttribute("boardData");

        // 목록 또는 상세 보기 이동
        String url = board.getLocationAfterWriting().equals("list") ? "/board/list/" + board.getBid() : "/board/view/" + boardData.getSeq();

        return "redirect:" + utils.redirectUrl(url);
    }

    /**
     * 게시판 목록
     * @param bid : 게시판 아이디
     * @param model
     * @return
     */
    @GetMapping("/list/{bid}")
    public String list(@PathVariable("bid") String bid, @ModelAttribute BoardDataSearch search, Model model) {
        commonProcess(bid, "list", model);

        ListData<BoardData> data = boardInfoService.getList(bid, search);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("board/list");
    }

    /**
     * 게시글 1개 보기
     *
     * @param seq : 게시글 번호
     * @param model
     * @return
     */
    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "view", model);

        //boardInfoService.get(seq);

        return utils.tpl("board/view");
    }

    /**
     * 게시글 삭제
     *
     * @param seq : 게시글 번호
     * @param model
     * @return
     */
    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "deelete", model);

        boardDeleteService.delete(seq);

        return utils.redirectUrl("/board/list/" + board.getBid());
    }


    /**
     * 게시판 설정이 필요한 공통 처리(모든 처리)
     * 게시판의 공통 처리 - 글목록, 글쓰기 등 게시판 ID가 있는 경우
     *
     * @param bid : 게시판 ID
     * @param mode
     * @param model
     */
    protected void commonProcess(String bid, String mode, Model model) {
        board = configInfoService.get(bid).orElseThrow(BoardNotFoundException::new); // 게시판 설정

        List<String> addCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        String pageTitle = board.getBName(); // 게시판명 - title 태그 제목

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

        if (skin.equals("walking")) {
            addScript.add("walking/map");
            addCommonScript.add("map");
        }

        // 게시글 제목으로 title을 표시 하는 경우
        if (List.of("view", "update", "delete").contains(mode)) {
            pageTitle = boardData.getSubject();
        }

        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("board", board); // 게시판 설정
        model.addAttribute("pageTitle", pageTitle);
    }

    /**
     * 게시판 공통 처리 : 게시글 보기, 게시글 수정 - 게시글 번호가 있는 경우
     *      - 게시글 조회 -> 게시판 설정
     *
     * 게시글 번호가 경로 변수로 들어오는 공통 처리
     *  게시판 설정 + 게시글 내용
     *
     * @param seq : 게시글 번호
     * @param mode
     * @param model
     */
    protected void commonProcess(Long seq, String mode, Model model) {
        // 게시글 조회(엔티티)
        boardData = boardInfoService.get(seq);

        model.addAttribute("boardData", boardData);

        commonProcess(boardData.getBoard().getBid(), mode, model);
    }
}