package org.hidog.config.controllers;

import lombok.RequiredArgsConstructor;
import org.hidog.config.services.ConfigInfoService;
import org.hidog.config.services.ConfigSaveService;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.hidog.order.constants.PayMethod;
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

    private final ConfigSaveService saveService;
    private final ConfigInfoService infoService;
    private final Utils utils;

    @ModelAttribute("subMenuCode")
    public String subMenuCode(){
        return "payment";
    }

    @ModelAttribute("payMethods")
    public List<String[]> payMethods(){
        return PayMethod.getList();
    }

    @GetMapping
    public String index(Model model){
        PaymentConfig form = infoService.get(subMenuCode(), PaymentConfig.class).orElseGet(PaymentConfig::new);
        model.addAttribute("paymentConfig", form);
        return "config/payment";
    }

    @PostMapping
    public String save(PaymentConfig form, Model model){

        saveService.save(subMenuCode(), form);
        model.addAttribute("message", "저장되었습니다.");

        return "redirect:" + utils.redirectUrl("/config/payment");
    }


}
