package org.g9project4.mypage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.g9project4.member.MemberUtil;
import org.g9project4.mypage.services.MyPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final MemberUtil memberUtil;
    @GetMapping
    public String index(Model model) {
        model.addAttribute("addCss", List.of("mypage/mypageStyle"));
        return "front/mypage/index";
    }

    @GetMapping("/info")
    public String info(Model model) {
        if (!memberUtil.isLogin()) {
            // 비로그인 상태일 때 처리
            return "redirect:/member/login";
        }

        RequestProfile profile = new RequestProfile();
        profile.setUserName(memberUtil.getMember().getUserName());

        model.addAttribute("profile", profile);
        model.addAttribute("addCss", List.of("mypage/style"));
        return "front/mypage/info";
    }


    @PostMapping("/info")
    public String updateInfo(@ModelAttribute("profile") @Valid RequestProfile profile,
                             Errors errors, Principal principal, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("addCss", List.of("mypage/style"));
            return "front/mypage/info";
        }

        myPageService.update(profile);
        return "redirect:/mypage/info";
    }
}
