package org.g9project4.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.g9project4.board.controllers.BoardDataSearch;
import org.g9project4.board.entities.Board;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.exceptions.BoardNotFoundException;
import org.g9project4.board.exceptions.GuestPasswordCheckException;
import org.g9project4.board.services.*;
import org.g9project4.board.validators.BoardValidator;
import org.g9project4.file.services.FileInfoService;
import org.g9project4.global.ListData;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.global.exceptions.UnAuthorizedException;
import org.g9project4.member.MemberUtil;
import org.g9project4.publicData.tour.controllers.TourPlaceSearch;

import org.g9project4.search.services.SearchHistoryService;
import org.g9project4.visitCount.VisitorCount;

import org.g9project4.visitCount.services.VisitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/")
@Controller
@RequiredArgsConstructor
@SessionAttributes({"boardData", "board"})
public class MainController implements ExceptionProcessor {
    private final BoardInfoService infoService;

    private final Utils utils;



    private final VisitorService visitorService;

    @GetMapping
    public String index(Model model, @ModelAttribute TourPlaceSearch search, @ModelAttribute BoardDataSearch Search) {
        String bid = "review";
        ListData<BoardData> data = infoService.getList(bid,Search);

        model.addAttribute("items", data.getItems());
        model.addAttribute("addCommonCss",List.of("banner"));
        model.addAttribute("addCss", "main"); // CSS 파일 목록
        model.addAttribute("addScript", "main"); // JS 파일 목록

        visitorService.recordVisit();

        return "front/main/index";
    }

}
