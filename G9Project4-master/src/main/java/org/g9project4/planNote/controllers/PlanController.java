package org.g9project4.planNote.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanController {

    private final Utils utils;
    @GetMapping
    public String plan() {
        return utils.tpl("plan/index");
    }
}
