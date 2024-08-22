package org.hidog.mypage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.hidog.member.services.MemberSaveService;
import org.hidog.mypage.validators.ProfileUpdateValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController implements ExceptionProcessor {

    private final ProfileUpdateValidator profileUpdateValidator;
    private final MemberSaveService saveService;
    private final Utils utils;

    // 마이 페이지 홈
    @GetMapping("/myhome")
    public String myHome() {

        return utils.tpl("mypage/myhome");
    }

    @GetMapping("/info")
    public String info(@ModelAttribute RequestProfile form) {

        return utils.tpl("mypage/info");
    }

    // 회원 정보 수정
    @PostMapping("/info")
    public String infoSave(@Valid RequestProfile form, Errors errors) {

        profileUpdateValidator.validate(form, errors);

        if (errors.hasErrors()) {
            return utils.tpl("mypage/info");
        }

        saveService.save(form);

        return "redirect:" + utils.redirectUrl("mypage/myhome");
    }
}