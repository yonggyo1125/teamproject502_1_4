package org.g9project4.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.Board;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.exceptions.BoardNotFoundException;
import org.g9project4.board.exceptions.GuestPasswordCheckException;
import org.g9project4.board.services.*;
import org.g9project4.board.validators.BoardValidator;
import org.g9project4.file.constants.FileStatus;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.file.services.FileInfoService;
import org.g9project4.global.ListData;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.CommonException;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.global.exceptions.UnAuthorizedException;
import org.g9project4.global.exceptions.script.AlertBackException;
import org.g9project4.member.MemberUtil;
import org.g9project4.search.services.SearchHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@SessionAttributes({"boardData", "board"})
public class BoardController implements ExceptionProcessor {
    private final BoardConfigInfoService configInfoService;
    private final BoardInfoService infoService;
    private final BoardSaveService saveService;
    private final BoardDeleteService deleteService;
    private final FileInfoService fileInfoService;
    private final SearchHistoryService historyService;
    private final BoardViewCountService viewCountService;
    private final BoardAuthService authService;

    private final BoardValidator validator;
    private final MemberUtil memberUtil;
    private final Utils utils;

    private Board board; // 게시판 설정
    private BoardData boardData; // 게시글 내용

    @ModelAttribute("mainClass")
    public String mainClass() {
        return "board-main layout-width";
    }

    /**
     * 글 쓰기
     * @param bid
     * @return
     */
    @GetMapping("/write/{bid}")
    public String write(@PathVariable("bid") String bid, @ModelAttribute RequestBoard form, Model model) {
        commonProcess(bid, "write", model);

        form.setGuest(!memberUtil.isLogin());
        if (memberUtil.isLogin()) form.setPoster(memberUtil.getMember().getUserName());

        return utils.tpl("board/write");
    }

    // 글 수정
    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "update", model);

        RequestBoard form = infoService.getForm(boardData);
        model.addAttribute("requestBoard", form);

        return utils.tpl("board/update");
    }

    // 글 작성, 수정 처리
    @PostMapping("/save")
    public String save(@Valid RequestBoard form, Errors errors, Model model, SessionStatus status, HttpSession session) {
        String mode = form.getMode();
        System.out.println(form);
        System.out.println(errors.getAllErrors().stream().toString());
        mode = mode != null && StringUtils.hasText(mode.trim()) ? mode.trim() : "write";
        commonProcess(form.getBid(), mode, model);
        boolean isGuest = (mode.equals("write") && !memberUtil.isLogin());
        BoardData data = null;
        if (mode.equals("update")) {
            data = (BoardData)model.getAttribute("boardData");
            isGuest = data.getMember() == null;
        }
        form.setGuest(isGuest);

        validator.validate(form, errors);
        if (errors.hasErrors()) {
            // 업로드된 파일 목록 - editor, attach
            String gid = form.getGid();
            List<FileInfo> editorImages = fileInfoService.getList(gid, "editor", FileStatus.ALL);
            List<FileInfo> attachFiles = fileInfoService.getList(gid, "attach", FileStatus.ALL);
            form.setEditorImages(editorImages);
            form.setAttachFiles(attachFiles);
            System.out.println(errors);

            return utils.tpl("board/" + mode);
        }

        saveService.save(form);


        status.setComplete();
        session.removeAttribute("boardData");

        // 목록 또는 상세 보기 이동
        if (board.getLocationAfterWriting().equals("admin") && !StringUtils.hasText(form.getLongText1())) {
            return "redirect:" + utils.redirectUrl("/board/list/" + board.getBid());
        } else if (board.getLocationAfterWriting().equals("admin") && StringUtils.hasText(form.getLongText1())) {
            return "redirect:" + utils.adminUrl("/");

        }

        String url = board.getLocationAfterWriting().equals("list") ? "/board/list/" + board.getBid() : "/board/view/" + boardData.getSeq();

        return "redirect:" + utils.redirectUrl(url);


    }

    @GetMapping("/list/{bid}")
    public String list(@PathVariable("bid") String bid, @ModelAttribute BoardDataSearch search, Model model) {
        commonProcess(bid, "list", model);

        historyService.saveBoard(search.getSkey());

        ListData<BoardData> data = infoService.getList(bid, search);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("board/list");
    }


    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, @ModelAttribute BoardDataSearch search, Model model) {
        commonProcess(seq, "view", model);
        System.out.println(board);
        if (board.isShowListBelowView()) { // 게시글 하단에 목록 보여주기
            ListData<BoardData> data = infoService.getList(board.getBid(), search);
            model.addAttribute("items", data.getItems());
            model.addAttribute("pagination", data.getPagination());
        }

        // 댓글 커맨드 객체 처리 S
        RequestComment requestComment = new RequestComment();
        if (memberUtil.isLogin()) {
            requestComment.setCommenter(memberUtil.getMember().getUserName());
        }

        model.addAttribute("requestComment", requestComment);
        // 댓글 커맨드 객체 처리 E

        viewCountService.update(seq); // 조회수 증가

        return utils.tpl("board/view");
    }

    // 게시글 삭제
    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "delete", model);

        deleteService.delete(seq);

        return "redirect:" + utils.redirectUrl("/board/list/" + board.getBid());
    }
    @GetMapping("/qna/answer/{seq}")
    public String answer(@PathVariable("seq") Long seq, Model model){
        commonProcess(seq, "update", model);
        RequestBoard form = infoService.getForm(boardData);
        model.addAttribute("requestBoard", form);
//        board.setLocationAfterWriting("admin");
//        model.addAttribute("locationAfterWriting", board.getLocationAfterWriting());

        return utils.tpl("board/qna/answer");
    }
    /**
     * 비회원 비밀번호 검증
     *
     * @param password
     * @param model
     * @return
     */
    @PostMapping("/password")
    public String confirmGuestPassword(@RequestParam("password") String password, Model model) {

        authService.validate(password, boardData);



        String script = "parent.location.reload();";
        model.addAttribute("script", script);

        return "common/_execute_script";
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

        String pageTitle = board.getBName(); // 게시판명 - title 태그 제목

        mode = mode == null || !StringUtils.hasText(mode.trim()) ? "write" : mode.trim();

        String skin = board.getSkin(); // 스킨

        // 게시판 공통 JS
        addCommonScript.add("wish");

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
            addScript.add("board/" + skin + "/answer");
        } else if (mode.equals("view")) { // 게시글 보기의 경우
            addScript.add("board/" + skin + "/view");
        }

        addCss.add("board/" + skin + "/form");


        // 게시글 제목으로 title을 표시 하는 경우
        if (List.of("view", "update", "delete").contains(mode)) {
            pageTitle = boardData.getSubject();
        }

        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("board", board); // 게시판 설정
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("mode", mode);

        //권한 체크
        if (List.of("write", "list").contains(mode)) {
            authService.check(mode, board.getBid());
        }
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
        model.addAttribute("board", boardData.getBoard());
        commonProcess(boardData.getBoard().getBid(), mode, model);

        authService.check(mode, seq); //권한 체크
    }

    @Override
    @ExceptionHandler(Exception.class)
    public ModelAndView errorHandler(Exception e, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (e instanceof UnAuthorizedException unAuthorizedException) {
            String message = unAuthorizedException.getMessage();
            message = unAuthorizedException.isErrorCode() ? utils.getMessage(message) : message;
            String script = String.format("alert('%s');history.back();", message);

            mv.setStatus(unAuthorizedException.getStatus());
            mv.setViewName("common/_execute_script");
            mv.addObject("script", script);

            return mv;
        } else if ( e instanceof GuestPasswordCheckException passwordCheckException) {

            mv.setStatus(passwordCheckException.getStatus());
            mv.addObject("board", board);
            mv.addObject("boardData", boardData);
            mv.setViewName(utils.tpl("board/password"));
            return mv;
        }

        return ExceptionProcessor.super.errorHandler(e, request);
    }
}