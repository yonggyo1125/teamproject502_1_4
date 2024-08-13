package org.g9project4.config.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.config.service.ConfigInfoService;
import org.g9project4.config.service.ConfigSaveService;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/config/payment")
@RequiredArgsConstructor
public class PaymentConfigController implements ExceptionProcessor, CommonConfig {
    private final ConfigInfoService infoService;
    private final ConfigSaveService saveService;

    @GetMapping
    public String index(@ModelAttribute PaymentConfig form) {
        return "config/payment";
    }

    @PostMapping
    public String save(PaymentConfig form) {

        return "config/payment";
    }
}
