package org.hidog.payment.controllers;

import lombok.RequiredArgsConstructor;
import org.hidog.global.Utils;
import org.hidog.global.exceptions.ExceptionProcessor;
import org.hidog.order.entities.OrderInfo;
import org.hidog.order.services.OrderPayService;
import org.hidog.payment.services.PaymentProcessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController implements ExceptionProcessor {

    private final PaymentProcessService processService;
    private final OrderPayService orderPayService;
    private final Utils utils;

    @ResponseBody
    @PostMapping("/process")
    public String process(PayAuthResult result) {

        PayConfirmResult confirmResult = processService.process(result);
        if(confirmResult == null){ //결제실패시에는 주문 실패 페이지로 이동
            return "redirect:" + utils.redirectUrl("/order/fail");
        }

        OrderInfo orderInfo = orderPayService.update(confirmResult);

        //주문 성공시에는 주문 상세 페이지로 이동.
        return "redirect:" + utils.redirectUrl("/order/detail/" + orderInfo.getOrderNo());
    }

    @RequestMapping("/close")
    public String close(Model model) {

        String script = "const modalBg = document.querySelector('.inipay_modal-backdrop');" +
                "const modal = document.getElementById('inicisModalDiv');" +
                "if (modalBg) modalBg.parentNode.removeChild(modalBg);" +
                "if (modal) modal.parentNode.removeChild(modal);";

        model.addAttribute("script", script);

        return "common/_execute_script";
    }
}
