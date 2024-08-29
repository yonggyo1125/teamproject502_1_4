package org.g9project4.publicData.tourvisit.controllers;


import lombok.RequiredArgsConstructor;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.BadRequestException;



//import org.g9project4.tourvisit.services.VisitInfoService;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.publicData.tour.controllers.TourPlaceSearch;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.services.TourPlaceInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//@Controller
//@RequestMapping("/tourvisit")
//@RequiredArgsConstructor
//public class TourVisitController implements ExceptionProcessor {
//
//
//  private final TourPlaceInfoService placeInfoService;
//    private final Utils utils;
//
//    private void addListProcess(Model model, ListData<TourPlace> data) {
//        Pagination pagination = data.getPagination();
//        //pagination.setBaseURL();
//        model.addAttribute("items", data.getItems());
//        model.addAttribute("pagination", pagination);
//    }
//
//    private void commonProcess(String mode, Model model) {
//        if (mode.equals("list")) {
//            model.addAttribute("addCss", List.of("tourvisit/list","tourvisit/_typelist"));
//            model.addAttribute("addScript", List.of("tourvisit/list"));
//        }
//    }
//
//    @GetMapping("/list")
//    public String list(Model model, @ModelAttribute TourPlaceSearch search) {
//        search.setContentType(null);
//        ListData<TourPlace> data = placeInfoService.getTotalVisitList(search);
//        commonProcess("list", model);
//        addListProcess(model, data);
//        return utils.tpl("tourvisit/list");
//    }
//
//
//    //추천
//    @GetMapping("/list/{type}")
//    public String list(@PathVariable("type") String type, @ModelAttribute TourPlaceSearch search, Model model) {
//        try {
//            search.setContentType ( utils.typeCode(type));
//
//
//            // Check if contentType is correctly set
//            if (search.getContentType() == null) {
//                throw new BadRequestException("ContentType is null");
//            }
//
//            ListData<TourPlace> data = placeInfoService.getTotalVisitList(search);
//            commonProcess("list", model);
//            addListProcess(model, data);
//            return utils.tpl("tourvisit/list");
//        } catch (BadRequestException e) {
//            e.printStackTrace();
//            return "redirect:" + utils.redirectUrl("tourvisit/list");
//        }
//    }
//
//
//}
