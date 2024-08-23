package org.hidog.mypage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.hidog.member.MemberUtil;
import org.hidog.member.entities.Member;
import org.hidog.member.services.MemberSaveService;
import org.hidog.mypage.validators.ProfileUpdateValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class MypageController implements ExceptionProcessor {
    private final ProfileUpdateValidator profileUpdateValidator;
    private final MemberSaveService saveService;
    private final MemberUtil memberUtil;
    private final Utils utils;


    @GetMapping("/myhome")
    public String myHome() {

        return utils.tpl("mypage/myhome");
    }

    @GetMapping("/info")
    public String info(@ModelAttribute RequestProfile form, Model model) {
        commonProcess("info", model);
        Member member = memberUtil.getMember();
        form.setUserName(member.getUserName());
        form.setAddress(member.getAddress());
        form.setDetailAddress(member.getDetailAddress());

        return utils.tpl("mypage/info");
    }

    @PostMapping("/info")
    public String infoSave(@Valid RequestProfile form, Errors errors, Model model) {
        commonProcess("info", model);

        profileUpdateValidator.validate(form, errors);

        if (errors.hasErrors()) {
            return utils.tpl("mypage/info");
        }

        saveService.save(form);

        return "redirect:" + utils.redirectUrl("/mypage/myhome");
    }

    private void commonProcess(String mode, Model model) {
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        List<String> addCss = new ArrayList<>();

        if (mode.equals("info")) {
            addCommonScript.add("fileManager");
            addScript.add("mypage/info");
        }

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
    }
}
