package org.g9project4.tour;

import org.g9project4.tourvisit.services.VisitStatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataTransfer {

    @Autowired
    private VisitStatisticService service;

    @Test
    void test1() {

        service.updateSidoVisit("1M");
    }
}
