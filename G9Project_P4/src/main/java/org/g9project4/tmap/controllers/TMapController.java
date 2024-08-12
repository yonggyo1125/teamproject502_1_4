package org.g9project4.tmap.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.Utils;
import org.g9project4.global.services.ApiConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tmap")
@RequiredArgsConstructor
public class TMapController {

    private final ApiConfigService apiConfigService;
    private final Utils utils;

    @ModelAttribute("tmapJavascriptKey")
    public String tmapJavascriptKey() {
        return apiConfigService.get("tmapJavascriptKey");
    }

    @GetMapping("/test")
    public String tmapTest(Model model) {

        model.addAttribute("addCommonScript", List.of("tmap"));
        model.addAttribute("addScript", List.of("tmap/test"));

        return utils.tpl("tmap/test");
    }
}
