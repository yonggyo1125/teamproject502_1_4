package org.g9project4.tour;

import org.g9project4.publicData.tour.services.PublicDataUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EventTest {
    @Autowired
    private PublicDataUpdateService service;

    @Test
    void test1() {
        service.start();
    }
}
