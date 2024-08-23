package org.hidog.member.controllers;

import lombok.RequiredArgsConstructor;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.hidog.menus.Menu;
import org.hidog.menus.MenuDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminMemberController implements ExceptionProcessor {

    @ModelAttribute("menuCode")
    public String getMenuCode() {
        return "member";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() {

        return Menu.getMenus("member");
    }

    @GetMapping("/member")
    public String list(Model model) {

        model.addAttribute("subMenuCode", "list");
        return "admin/member" ;
    }

    @PostMapping("/member")
    public String list2(Model model) {

        model.addAttribute("subMenuCode", "list");
        return "admin/member" ;
    }


}
