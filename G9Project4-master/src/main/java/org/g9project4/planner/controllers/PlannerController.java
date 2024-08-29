package org.g9project4.planner.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.ListData;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.planner.entities.Planner;
import org.g9project4.planner.services.PlannerInfoService;
import org.g9project4.planner.services.PlannerSaveService;
import org.g9project4.publicData.tour.controllers.TourPlaceSearch;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.services.NewTourPlaceInfoService;
import org.g9project4.publicData.tour.services.TourPlaceInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/planner")
@RequiredArgsConstructor
public class PlannerController implements ExceptionProcessor {

    private final PlannerSaveService saveService;
    private final TourPlaceInfoService tourPlaceInfoService;
    private final PlannerInfoService infoService;
    private final Utils utils;
    private final NewTourPlaceInfoService newTourPlaceInfoService;

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

        RequestPlanner form = infoService.getForm(seq);
        String item = form.getItinerary();
        if (StringUtils.hasText(item)) {
            List<Map<String, String>> items = utils.toList(item);
            model.addAttribute("items", items);
        }
        model.addAttribute("requestPlanner", form);
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
            String item = form.getItinerary();
            if (StringUtils.hasText(item)) {
                List<Map<String, String>> items = utils.toList(item);
                model.addAttribute("items", items);
            }
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
    public String list(@ModelAttribute PlannerSearch search, Model model) {
        commonProcess("list", model);

        ListData<Planner> data = infoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());
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

        Planner data = infoService.get(seq);
        model.addAttribute("data", data);
        return utils.tpl("planner/view");
    }

    /**
     * 여행지 선택
     *
     * @param mode
     * @param model
     * @return
     */
    @GetMapping("/select/{mode}")
    public String select(@PathVariable("mode") String mode, @ModelAttribute TourPlaceSearch search, Model model) {
        commonProcess("select_" + mode, model);
        mode = StringUtils.hasText(mode) ? mode : "tourplace";

        if (mode.equals("tourplace")) {
            ListData<TourPlace> data = newTourPlaceInfoService.getSearchedList(search);
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

        String pageTitle = utils.getMessage("여행_플래너");
        // 플래너 작성, 수정

        if (List.of("write", "update").contains(mode)) {
            addCss.add("planner/form");
            addScript.add("planner/form");
            pageTitle += " " + utils.getMessage(mode.equals("update") ? "수정" : "작성");
        } else if (mode.equals("select_tourplace")) { // 여행지 선택
            addCss.add("planner/select_tourplace");
            addScript.add("planner/select_tourplace");
        } else if (mode.equals("list")) {
            pageTitle = utils.getMessage("나의_여행_플래너_목록");
        }
        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("pageTitle",pageTitle);
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