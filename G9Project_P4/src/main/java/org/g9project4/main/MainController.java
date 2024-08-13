package org.g9project4.main;

import lombok.RequiredArgsConstructor;
import org.g9project4.order.services.PaymentConfig;
import org.g9project4.order.services.PaymentConfigService;
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

        long oid = 100000L;
        int price = 100;

        PaymentConfig config = configService.get(oid, price);

        System.out.println(config);

        model.addAttribute("config", config);
        model.addAttribute("oid", oid);
        model.addAttribute("price", price);

        return "front/main/index";
    }
}
