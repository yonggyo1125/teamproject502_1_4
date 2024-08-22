package org.hidog.order.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hidog.board.entities.BoardData;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.hidog.order.entities.OrderInfo;
import org.hidog.order.services.OrderInfoService;
import org.hidog.order.services.OrderPayService;
import org.hidog.order.services.OrderSaveService;
import org.hidog.payment.constants.PayMethod;
import org.hidog.payment.services.PaymentConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController implements ExceptionProcessor {

    private final Utils utils;
    private final OrderInfoService infoService;
    private final OrderSaveService saveService;
    private final OrderPayService payService;

    @ModelAttribute("payMethods")
    public List<String[]> payMethods(){
        return PayMethod.getList();
    }

    @GetMapping //주문서양식
    public String index(@ModelAttribute RequestOrder form, HttpSession session, Model model){
        BoardData boardData = (BoardData) session.getAttribute("boardData");
        model.addAttribute("addCss", "order/style");
        model.addAttribute("addScript", "order/joinAddress");


        if (boardData != null) {
            form.setBSeq(boardData.getSeq());
            model.addAttribute("boardData", boardData);
        }
        return utils.tpl("order/form");
    }

    @PostMapping
    public String orderSave(@Valid RequestOrder form, Errors erros, Model model, HttpSession session){
        BoardData boardData = (BoardData) session.getAttribute("boardData");
        if (boardData != null) {
            form.setBSeq(boardData.getSeq());
            model.addAttribute("boardData", boardData);
        }
        OrderInfo orderInfo = saveService.save(form);
        if(!erros.hasErrors()){
            PaymentConfig config = payService.getConfig(orderInfo.getOrderNo());
            model.addAttribute("config", config);
            model.addAttribute("orderInfo", orderInfo);
        }

        return utils.tpl("order/form");
    }

    @GetMapping("/detail/{orderNo}")
    public String orderDetail(@PathVariable("orderNo") Long orderNo, Model model){
        OrderInfo orderInfo = infoService.get(orderNo, "detail");
        model.addAttribute("orderInfo", orderInfo);
        return utils.tpl("order/detail");
    }
}
