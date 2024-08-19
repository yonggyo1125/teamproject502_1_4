package org.g9project4.order.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.order.entities.OrderInfo;
import org.g9project4.order.services.OrderPayService;
import org.g9project4.order.services.OrderSaveService;
import org.g9project4.payment.constants.PayMethod;
import org.g9project4.payment.services.PaymentConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController implements ExceptionProcessor {
    private final OrderSaveService saveService;
    private final OrderPayService payService;
    private final Utils utils;

    @ModelAttribute("payMethods")
    public List<String[]> payMethods() {
        return PayMethod.getList();
    }

    @GetMapping // 주문서 양식
    public String index(@ModelAttribute RequestOrder form) {

        return utils.tpl("order/form");
    }

    @PostMapping
    public String orderSave(@Valid RequestOrder form, Errors errors, Model model) {

        OrderInfo orderInfo = saveService.save(form);

        if (!errors.hasErrors()) {
            PaymentConfig config = payService.getConfig(orderInfo.getOrderNo());
            model.addAttribute("config", config);
            model.addAttribute("orderInfo", orderInfo);
        }


        return utils.tpl("order/form");

    }
}
