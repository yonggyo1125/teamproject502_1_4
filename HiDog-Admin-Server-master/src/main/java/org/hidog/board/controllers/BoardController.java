package org.hidog.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hidog.board.entities.Board;
import org.hidog.board.services.BoardConfigDeleteService;
import org.hidog.board.services.BoardConfigInfoService;
import org.hidog.board.services.BoardConfigSaveService;
import org.hidog.board.validators.BoardConfigValidator;
import org.hidog.global.ListData;
import org.hidog.global.Pagination;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.hidog.menus.Menu;
import org.hidog.menus.MenuDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController implements ExceptionProcessor {

    private final BoardConfigSaveService configSaveService;
    private final BoardConfigInfoService configInfoService;
    private final BoardConfigDeleteService configDeleteService;

    private final BoardConfigValidator validator;

    @ModelAttribute
    public RequestBoardConfig requestBoardConfig() {
        return new RequestBoardConfig();
    }


    private final BoardConfigValidator configValidator;
    private final Utils utils;

    @ModelAttribute("menuCode")
    public String getMenuCode() { // 주 메뉴 코드
        return "board";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() { // 서브 메뉴
        return Menu.getMenus("board");
    }


    /**
     * 게시판 목록 페이지
     * @param model
     * @return
     */
    @GetMapping
    public String list(@ModelAttribute BoardSearch search, Model model) {
        commonProcess("list", model);

        ListData<Board> data = configInfoService.getList(search, true);

        List<Board> items = data.getItems();
        Pagination pagination = data.getPagination();

        model.addAttribute("items", items);
        model.addAttribute("pagination", pagination);

        return "/board/list";
    }

    /**

     * 게시판 다중 수정
     * @return
     */
    @PatchMapping
    public String editList(@RequestParam("chk") List<Integer> chks, Model model) {
        commonProcess("list", model);
        configSaveService.saveList(chks);
        model.addAttribute("script", "parent.location.reload()");
        return "common/_execute_script";
    }


    /**
     * 게시판 다중 삭제
     * @return
     */
    @DeleteMapping
    public String deleteList(@RequestParam("chk") List<Integer> chks, Model model){
        commonProcess("list", model);
        chks.forEach(System.out::println);
        configDeleteService.deleteList(chks);
        model.addAttribute("script", "parent.location.reload()");
        return "common/_execute_script";
    }

    /**
     * 게시판 등록 페이지
     * @param config
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(@ModelAttribute RequestBoardConfig config, Model model) {
        commonProcess("add", model);
        return "/board/add";
    }


    /**
     * 게시판 수정 페이지
     * @param bid
     * @param model
     * @return
     */
    @GetMapping("/edit/{bid}")
    public String edit(@PathVariable("bid") String bid, Model model) {
        commonProcess("edit", model);

        RequestBoardConfig form = configInfoService.getForm(bid);
        model.addAttribute("requestBoardConfig", form);

        return "/board/edit";
    }

    /**
     * 게시글 등록 or 수정 처리
     * @param config
     * @param model
     * @param errors
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestBoardConfig config, Errors errors, Model model) {
        String mode = config.getMode();

        commonProcess(mode, model);

        configValidator.validate(config, errors);

        if (errors.hasErrors()) {
            return "board/" + mode;
        }

        configSaveService.save(config);


        return "redirect:" + utils.redirectUrl("/board");
    }

    /**
     * 게시글 관리
     * @param model
     * @return
     */
    @GetMapping("/posts")
    public String posts(Model model) {
        commonProcess("posts", model);

        return "/board/posts";
    }

    /**
     * 공통 처리
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        String pageTitle = "게시판 목록";
        mode = StringUtils.hasText(mode) ? mode : "list";

        if (mode.equals("add")) {
            pageTitle = "게시판 등록";

        } else if (mode.equals("edit")) {
            pageTitle = "게시판 수정";

        } else if (mode.equals("posts")) {
            pageTitle = "게시글 관리";

        }

        List<String> addScript = new ArrayList<>();

        if (mode.equals("add") || mode.equals("edit")) { // 게시판 등록 또는 수정
            addScript.add("ckeditor5/ckeditor");
            addScript.add("fileManager");

            addScript.add("board/form");
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("subMenuCode", mode);
        model.addAttribute("addScript", addScript);
    }
}
