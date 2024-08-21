package org.hidog.order.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.hidog.order.entities.OrderInfo;
import org.hidog.order.services.OrderPayService;
import org.hidog.order.services.OrderSaveService;
import org.hidog.payment.constants.PayMethod;
import org.hidog.payment.services.PaymentConfig;
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

    private final Utils utils;
    private final OrderSaveService saveService;
    private final OrderPayService payService;

    @ModelAttribute("payMethods")
    public List<String[]> payMethods(){
        return PayMethod.getList();
    }

    @GetMapping //주문서양식
    public String index(@ModelAttribute RequestOrder form){

        return utils.tpl("order/form");
    }

    @PostMapping
    public String orderSave(@Valid RequestOrder form, Errors erros, Model model){
        OrderInfo orderInfo = saveService.save(form);
        if(!erros.hasErrors()){
            PaymentConfig config = payService.getConfig(orderInfo.getOrderNo());
            model.addAttribute("config", config);
            model.addAttribute("orderInfo", orderInfo);
        }

        return utils.tpl("order/form");
    }
}
