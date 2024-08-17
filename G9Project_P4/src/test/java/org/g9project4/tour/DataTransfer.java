package org.g9project4.tour;

import org.g9project4.tourvisit.services.SidoVisitStatisticService;
import org.g9project4.tourvisit.services.SigunguVistStatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataTransfer {

    @Autowired
    private SidoVisitStatisticService service;

    @Autowired
    private SigunguVistStatisticService service2;

    @Test
    void test1() {
        service.updateSidoVisit("6M");
        //service.updateSidoVisit("1M");
    }

    @Test
    void test2() {
        service2.updateVisit("1M");
    }
}
