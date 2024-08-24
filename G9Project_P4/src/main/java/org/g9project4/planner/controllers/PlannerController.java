package org.g9project4.planner.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.ListData;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.planner.services.PlannerSaveService;
import org.g9project4.publicData.tour.controllers.TourPlaceSearch;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.services.TourPlaceInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/planner")
@RequiredArgsConstructor
public class PlannerController implements ExceptionProcessor {

    private final PlannerSaveService saveService;
    private final TourPlaceInfoService tourPlaceInfoService;
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
    public String write(@ModelAttribute RequestPlanner form, Model model) {
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
    public String save(@Valid RequestPlanner form, Errors errors, Model model) {
        String mode = StringUtils.hasText(form.getMode()) ? form.getMode() : "write";
        commonProcess(mode, model);

        if (errors.hasErrors()) {
            return utils.tpl("planner/" + mode);
        }

        // 저장 처리
        saveService.save(form);

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
     * 여행지 선택
     * @param mode
     * @param model
     * @return
     */
    @GetMapping("/select/{mode}")
    public String select(@PathVariable("mode") String mode, @ModelAttribute TourPlaceSearch search, Model model) {
        commonProcess("select_" + mode, model);
        mode = mode = StringUtils.hasText(mode) ? mode : "tourplace";

        if (mode.equals("tourplace")) {
            ListData<TourPlace> data = tourPlaceInfoService.getTotalList(search);
            model.addAttribute("items", data.getItems());
            model.addAttribute("pagination", data.getPagination());
        }

        return utils.tpl("planner/select_" + mode);
    }

    /**
     * 공통 처리
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "write";

        List<String> addCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        addCss.add("planner/style");

        // 플래너 작성, 수정
        if (List.of("write", "update").contains(mode)) {
            addCss.add("planner/form");
            addScript.add("planner/form");
        } else if (mode.equals("select_tourplace")) { // 여행지 선택
            addCss.add("planner/select_tourplace");
            addScript.add("planner/select_tourplace");
        }

        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
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
