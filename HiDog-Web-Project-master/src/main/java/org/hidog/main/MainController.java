package org.hidog.main;

import lombok.RequiredArgsConstructor;
import org.hidog.board.entities.Board;
import org.hidog.board.services.BoardConfigInfoService;
import org.hidog.payment.services.PaymentConfig;
import org.hidog.payment.services.PaymentConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final PaymentConfigService configService;

    private final BoardConfigInfoService configInfoService;

    @GetMapping("/checkout")
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



    @GetMapping("/main/index2")
    public String boardList(Model model) {

        Optional<Board> boardList = configInfoService.getBoardList();
        model.addAttribute("boardList", boardList);

        model.addAttribute("addCss", List.of("main/style"));
        model.addAttribute("addScript", List.of( "main/dropdown"));

        model.addAttribute("addCommonCss", List.of("swiper-bundle.min"));
        model.addAttribute("addScript", List.of( "main/banner"));
        model.addAttribute("addCommonScript", List.of("swiper-bundle.min"));

        return "front/main/index2";
    }
}
