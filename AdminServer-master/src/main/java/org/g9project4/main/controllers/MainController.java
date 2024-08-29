package org.g9project4.main.controllers;

import lombok.RequiredArgsConstructor;

import org.g9project4.board.controllers.BoardDataSearch;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.services.BoardInfoService;
import org.g9project4.global.ListData;
import org.g9project4.main.entities.VisitorCount;
import org.g9project4.main.services.VisitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final VisitorService visitorService;
    private final BoardInfoService boardInfoService;





    @GetMapping
    public String index(@ModelAttribute BoardDataSearch search, Model model) {
        String bid = "qna";
        List<String> addCss = new ArrayList<>();
        addCss.add("main/style");
        List<String> addScript = new ArrayList<>();
        addScript.add("main/chart");
        model.addAttribute("addCss", addCss);
        model.addAttribute("addScript", addScript);
        try {
            ListData<BoardData> data = boardInfoService.getListQna(search);
            model.addAttribute("items", data.getItems());
            model.addAttribute("pagination", data.getPagination());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ID 가 'qna'인 게시판을 만드세요");
        }


//        visitorService.recordVisit();
        List<VisitorCount> stats = visitorService.getVisitorStatistics();

        model.addAttribute("stats", stats);
        return "main/index";
    }



}
