package org.g9project4.main;

import lombok.RequiredArgsConstructor;
import org.g9project4.payment.services.PaymentConfig;
import org.g9project4.payment.services.PaymentConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private final PaymentConfigService configService;

    @GetMapping
    public String index(Model model) {



        return "front/main/index";
    }
}
