package org.g9project4.payment.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.payment.services.PaymentProcessService;
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

    @ResponseBody
    @PostMapping("/process")
    public void process(PayAuthResult result) {

        processService.process(result);

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
