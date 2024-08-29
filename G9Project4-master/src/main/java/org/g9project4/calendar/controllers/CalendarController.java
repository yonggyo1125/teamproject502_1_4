package org.g9project4.calendar.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.calendar.Calendar;
import org.g9project4.global.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final Calendar calendar;
    private final Utils utils;


    @GetMapping
    public String index(@ModelAttribute RequestCalendar search, Model model) {
        Integer year = search.getYear();
        Integer month = search.getMonth();

        LocalDate sDate = search.getSDate();
        LocalDate eDate = search.getEDate();

        Map<String, Object> data = calendar.getData(year, month, sDate, eDate);
        model.addAllAttributes(data);


        model.addAttribute("addCss", new String[]{"calendar/style"});
        model.addAttribute("addCommonScript", new String[]{"calendar/calendar"});

        return utils.tpl("calendar/index");
    }
}


