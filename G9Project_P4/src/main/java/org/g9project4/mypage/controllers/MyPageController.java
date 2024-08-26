package org.g9project4.mypage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.services.BoardInfoService;
import org.g9project4.global.CommonSearch;
import org.g9project4.global.ListData;
import org.g9project4.global.Utils;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.entities.Member;
import org.g9project4.member.services.MemberSaveService;
import org.g9project4.mypage.validators.ProfileUpdateValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final ProfileUpdateValidator profileUpdateValidator;
    private final MemberSaveService memberSaveService;
    private final BoardInfoService boardInfoService;
    private final MemberUtil memberUtil;
    private final Utils utils;

    @GetMapping
    public String index(Model model) {
        commonProcess("main", model);
        return utils.tpl("mypage/index");
    }

    @GetMapping("/info")
    public String info(@ModelAttribute RequestProfile form, Model model) {
        commonProcess("info", model);
        Member member = memberUtil.getMember();
        form.setUserName(member.getUserName());
        form.setMobile(member.getMobile());

        return utils.tpl("mypage/info");
    }


    @PostMapping("/info")
    public String updateInfo(@Valid RequestProfile form,
                             Errors errors, Model model) {
        commonProcess("info", model);

        profileUpdateValidator.validate(form, errors);

        if (errors.hasErrors()) {
            utils.tpl("mypage/info");
        }

        memberSaveService.save(form);


        return "redirect:" + utils.redirectUrl("/mypage");
    }

    /**
     * 내가 쓴 게시글
     *
     * @return
     */
    @GetMapping("/mypost")
    public String myPost(@ModelAttribute CommonSearch search, Model model) {
        ListData<BoardData> data = boardInfoService.getMyList(search);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("mypage/myposts");
    }

    /**
     * 내가 찜한 게시글
     * @return
     */
    @GetMapping("/wishlist")
    public String wishlist(@ModelAttribute CommonSearch search, Model model) {
        ListData<BoardData> data = boardInfoService.getWishList(search);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("mypage/wishlist");
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "main";

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        List<String> addCss = new ArrayList<>();
        addCss.add("mypage/style");

        if (mode.equals("info")) {
            addCommonScript.add("fileManager");
            addScript.add("mypage/info");
            addCss.add("mypage/info");
        }

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
    }
}
