package org.g9project4.planner.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/planner")
@RequiredArgsConstructor
public class PlannerController implements ExceptionProcessor {

    private final Utils utils;
    
    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return utils.getMessage("여행_플래너");
    }
    
    /**
     * 여행 플래너 작성
     *
     * @return
     */
    @GetMapping("/write")
    public String write(Model model) {
        commonProcess("write", model);

        return utils.tpl("planner/write");
    }

    /**
     * 여행 플래너 수정 
     * 
     * @param seq
     * @return
     */
    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "update", model);

        return utils.tpl("planner/update");
    }

    /**
     * 플래너 작성, 수정 처리
     * 
     * @return
     */
    @PostMapping("/save")
    public String save() {

        return "redirect:" + utils.redirectUrl("/planner");
    }

    /**
     * 여행 플래너 목록 
     * 
     * @return
     */
    @GetMapping
    public String list(Model model) {
        commonProcess("list", model);

        return utils.tpl("planner/list");
    }

    /**
     * 여행 플래너 보기 
     * 
     * @param seq
     * @return
     */
    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "view", model);

        return utils.tpl("planner/view");
    }

    /**
     * 공통 처리
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {

    }

    /**
     * 플래너 수정, 보기 공통 처리
     *
     * @param seq
     * @param mode
     * @param model
     */
    private void commonProcess(Long seq, String mode, Model model) {

        commonProcess(mode, model);
    }
}
