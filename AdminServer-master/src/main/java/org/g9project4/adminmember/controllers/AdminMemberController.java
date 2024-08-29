package org.g9project4.adminmember.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.math.raw.Mod;
import org.g9project4.adminmember.constants.Authority;
import org.g9project4.adminmember.services.AllMemberConfigInfoService;
import org.g9project4.adminmember.services.MemberConfigDeleteService;
import org.g9project4.adminmember.services.MemberConfigSaveService;
import org.g9project4.board.services.BoardConfigDeleteService;
import org.g9project4.global.ListData;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.member.entities.Member;
import org.g9project4.member.services.MemberInfoService;
import org.g9project4.menus.Menu;
import org.g9project4.menus.MenuDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/manager/member")
@RequiredArgsConstructor
public class AdminMemberController implements ExceptionProcessor {
    private final AllMemberConfigInfoService memberConfigInfoService;

    private final MemberInfoService infoService;
    private final MemberConfigSaveService memberConfigSaveService;
    private final Utils utils;
    private final MemberConfigDeleteService memberConfigDeleteService;

    @ModelAttribute("menuCode")
    public String getMenuCode() {
        return "member";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() {

        return Menu.getMenus("member");
    }

    @ModelAttribute("memberAuthorities")
    public List<String[]> memberAuthorities() {

        return Authority.getList(); //enum 상수 String으로...
    }
    @ModelAttribute("authorityList")
    public List<Authority> getAuthorityList() {
        return Authority.getAuthorities();
    }

    @GetMapping
    public String list(@ModelAttribute MemberSearch search, Model model) {
        commonProcess("list", model);
        if(search != null && StringUtils.hasText(search.getSopt()) && StringUtils.hasText(search.getSkey())){
            List<Member> searchResults = infoService.searchMember(search);
            model.addAttribute("items", searchResults);
        }else {
            ListData<Member> data = infoService.getList(search);
            List<Member> items = data.getItems();
            model.addAttribute("items", items);

            model.addAttribute("items", data.getItems()); // 목록
            model.addAttribute("pagination", data.getPagination()); // 페이징
        }

        return "adminMember/member/list";
    }

    @GetMapping("/edit/{email}")
    public String edit(@PathVariable("email") String email,@ModelAttribute RequestMember form, Model model) {
        commonProcess("edit", model);
        form = memberConfigInfoService.getForm(email);
        form.setAuthorities(form.getAuthorities());
        model.addAttribute("requestMember", form);
        return "adminMember/member/edit";
    }


    @PostMapping("/save")
    public String save(@Valid RequestMember form, Errors errors, Model model) {
        String mode = form.getMode();
        commonProcess(mode, model);
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(System.out::println);
            return "adminMember/member/" + mode;
        }
        memberConfigSaveService.save(form);
        return "redirect:" + utils.redirectUrl("/manager/member");

    }

    @PostMapping("/delete")
    public String delete(RequestMember form, Model model) {
        String email = form.getEmail();
        System.out.println("Email to delete: " + email);


        memberConfigDeleteService.delete(email);


        return "redirect:" + utils.redirectUrl("/manager/member");
    }

    @PatchMapping
    public String editList(@RequestParam("chk") List<Integer> chks, Model model) {
        commonProcess("list", model);

        memberConfigSaveService.saveList(chks);
        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }

    @DeleteMapping
    public String deleteList(@RequestParam("chk") List<Integer> chks, Model model) {
        commonProcess("list", model);
        memberConfigDeleteService.deleteList(chks);
        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }


    private void commonProcess(String mode, Model model) {
        mode = Objects.requireNonNullElse(mode, "list");
        String pageTitle = "회원 목록";
        if (mode.equals("edit")) {
            pageTitle = "회원 수정";
        } else if (mode.equals("add")) {
            pageTitle = "회원 등록";
        }
        List<String> addScript = new ArrayList<>();

        model.addAttribute("subMenuCode", mode);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addScript", addScript);
    }
}