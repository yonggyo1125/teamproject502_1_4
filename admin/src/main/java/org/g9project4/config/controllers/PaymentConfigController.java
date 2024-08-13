package org.g9project4.config.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.config.service.ConfigInfoService;
import org.g9project4.config.service.ConfigSaveService;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.order.constants.PayMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/config/payment")
@RequiredArgsConstructor
public class PaymentConfigController implements ExceptionProcessor, CommonConfig {
    private final ConfigInfoService infoService;
    private final ConfigSaveService saveService;

    @ModelAttribute("subMenuCode")
    public String subMenuCode() {
        return "payment";
    }

    @ModelAttribute("payMethods")
    public List<String[]> payMethods() {
        return PayMethod.getList();
    }

    @GetMapping
    public String index(Model model) {

        PaymentConfig form = infoService.get(subMenuCode(), PaymentConfig.class).orElseGet(PaymentConfig::new);

        model.addAttribute("paymentConfig", form);

        return "config/payment";
    }

    @PostMapping
    public String save(PaymentConfig form, Model model) {

        saveService.save(subMenuCode(), form);

        model.addAttribute("message", "저장되었습니다.");

        return "config/payment";
    }
}
