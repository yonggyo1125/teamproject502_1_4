package org.g9project4.tour;

import org.g9project4.publicData.tour.services.ApiUpdateService;
import org.g9project4.publicData.tour.services.VisitUpdateService;
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

    @Autowired
    private ApiUpdateService apiUpdateService;

    @Autowired
    private VisitUpdateService visitUpdateService;

    @Test
    void test1() {
        service.updateSidoVisit("6M");
        //service.updateSidoVisit("1M");
    }

    @Test
    void test2() {
        service2.updateVisit("1M");
    }

    @Test
    void test3() {
        //apiUpdateService.update();
        visitUpdateService.update();
    }
}
