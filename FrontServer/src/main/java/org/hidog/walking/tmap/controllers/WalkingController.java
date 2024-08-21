package org.hidog.walking.tmap.controllers;

import lombok.RequiredArgsConstructor;
import org.hidog.global.Utils;
import org.hidog.global.services.ApiConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/walking")
@RequiredArgsConstructor
public class WalkingController {

    private final ApiConfigService apiConfigService;
    private final Utils utils;

    @ModelAttribute("tmapJavascriptKey")
    public String tmapJavascriptKey() {
        return apiConfigService.get("tmapJavascriptKey");
    }

    @GetMapping("/map")
    public String tmapTest(Model model) {
        model.addAttribute("addCommonCss", List.of("map"));
        model.addAttribute("addCommonScript", List.of("map"));
        model.addAttribute("addScript", List.of("walking/map"));


        return utils.tpl("walking/map");
    }
}
