package org.hidog.config.controllers;

import lombok.RequiredArgsConstructor;
import org.hidog.config.services.ConfigInfoService;
import org.hidog.config.services.ConfigSaveService;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/config")
@RequiredArgsConstructor
public class BasicConfigController implements ExceptionProcessor, CommonConfig {

    private final ConfigSaveService saveService;
    private final ConfigInfoService infoService;
    private final Utils utils;

    @ModelAttribute("subMenuCode")
    public String getSubMenuCode() {
        return "basic";
    }

    @ModelAttribute("pageTitle")
    public String getPageTitle() {
        return "기본설정";
    }

    @GetMapping
    public String index(Model model) {

        BasicConfig config = infoService.get("basic", BasicConfig.class).orElseGet(BasicConfig::new);

        model.addAttribute("basicConfig", config);

        return "config/basic";
    }

    @PostMapping
    public String save(BasicConfig config, Model model) {

        saveService.save("basic", config);

        model.addAttribute("message", "저장되었습니다.");

        return utils.redirectUrl("config/basic");
    }
}
