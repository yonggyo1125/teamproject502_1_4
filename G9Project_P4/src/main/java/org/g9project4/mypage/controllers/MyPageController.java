package org.g9project4.mypage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.entities.Member;
import org.g9project4.member.services.MemberSaveService;
import org.g9project4.mypage.validators.ProfileUpdateValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final ProfileUpdateValidator profileUpdateValidator;
    private final MemberSaveService memberSaveService;
    private final MemberUtil memberUtil;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("addCss", List.of("mypage/mypageStyle"));
        return "front/mypage/index";
    }

    @GetMapping("/info")
    public String info(@ModelAttribute RequestProfile form) {

        Member member = memberUtil.getMember();
        form.setUserName(member.getUserName());
        form.setMobile(member.getMobile());

        return "front/mypage/info";
    }


    @PostMapping("/info")
    public String updateInfo(@Valid RequestProfile form,
                             Errors errors) {

        profileUpdateValidator.validate(form, errors);

        if (errors.hasErrors()) {
            return "front/mypage/info";
        }

        memberSaveService.save(form);



        //SecurityContextHolder.getContext().setAuthentication();

        return "redirect:/mypage";
    }
}
