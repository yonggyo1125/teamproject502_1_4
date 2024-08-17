package org.g9project4.tour;

import org.g9project4.tourvisit.services.VisitStatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class DataTransfer {

    @Autowired
    private VisitStatisticService service;

    @Test
    void test1() {
        LocalDate edate = LocalDate.now();
        LocalDate sdate = edate.minusMonths(1L);

        service.updateMetcoRegnVisit(1, sdate, edate);
    }
}
