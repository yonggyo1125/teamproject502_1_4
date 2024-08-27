package org.g9project4.publicData.tour.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.search.services.SearchHistoryService;
import org.g9project4.visitrecord.services.VistRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController implements ExceptionProcessor {
    private final Utils utils;
    private final VistRecordService recordService;
    private final SearchHistoryService historyService;

    @GetMapping("/list")
    public String list(Model model, @ModelAttribute TourPlaceSearch search) {


        // 키워드 검색 저장
        if (StringUtils.hasText(search.getSkey())) {
            historyService.saveTour(search.getSkey());
        }
        return utils.tpl("tour/list");
    }

    @GetMapping("/detail/{contentId}")
    public String detail(@PathVariable("contentId") Long contentId, Model model) {

        // 추천 방문 데이터 저장
        recordService.record(contentId);
        return utils.tpl("tour/detail");
    }
}
