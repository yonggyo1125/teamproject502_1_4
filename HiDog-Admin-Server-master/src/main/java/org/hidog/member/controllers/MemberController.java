package org.hidog.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.hidog.member.services.MemberSaveService;
import org.hidog.member.services.MemberService;
import org.hidog.member.validators.JoinValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@SessionAttributes("requestLogin")
public class MemberController implements ExceptionProcessor {

    private final JoinValidator joinValidator;
    private final MemberSaveService memberSaveService;
    private final MemberService memberService;
    private final Utils utils;


    @ModelAttribute
    public RequestLogin requestLogin() {
        return new RequestLogin();
    }

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form, Model model) {
        commonProcess("join", model);

        model.addAttribute("EmailAuthVerified", false);
        return utils.frontUrl("member/join");
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors, Model model) {
        commonProcess("join", model);

        joinValidator.validate(form, errors);

        if (errors.hasErrors()) {
            model.addAttribute(form);
            return utils.frontUrl("member/join");
        }

        memberSaveService.save(form);

        return "redirect:" + utils.redirectUrl("/member/login");
    }

    @GetMapping("/join/check-username")
    public ResponseEntity<Boolean> checkUserName(@RequestParam("userName") String userName) {
        boolean exists = memberService.existsByUserName(userName);
        return ResponseEntity.ok(exists); // exists가 true이면 중복, false이면 사용 가능
    }

    @GetMapping("/login")
    public String login(@ModelAttribute RequestLogin form, Errors errors, Model model) {
        commonProcess("login", model);

        String code = form.getCode();
        if (StringUtils.hasText(code)) {
            errors.reject(code, form.getDefaultMessage());
            //비번이 만료인 경우 비번 재설정 페이지 이동
            if (code.equals("CredentialsExpired.Login")) {
                return "redirect:" + utils.redirectUrl("/member/password/reset");
            }
        }
        return utils.frontUrl("member/login");
    }



    /**
     * 회원 관련 컨트롤러 공통 처리
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        mode = Objects.requireNonNullElse(mode, "join");

        List<String> addCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        addCss.add("member/style");  // 회원 공통 스타일
        if (mode.equals("join")) {
            addCommonScript.add("fileManager");
            addCss.add("member/join");
            addScript.add("member/join");
            addScript.add("member/joinAddress");
            addScript.add("member/joinNickName");

        } else if (mode.equals("login")) {
            addCss.add("member/login");
        }

        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
    }
}